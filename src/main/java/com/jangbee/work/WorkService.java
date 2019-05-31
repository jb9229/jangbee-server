package com.jangbee.work;

import com.google.firebase.database.*;
import com.jangbee.accounts.AccountDto;
import com.jangbee.ad.AdWithdrawHistRepository;
import com.jangbee.common.JangbeeNoticeService;
import com.jangbee.expo.ExpoNotiData;
import com.jangbee.expo.ExpoNotificationService;
import com.jangbee.firm.Firm;
import com.jangbee.firm.FirmService;
import com.jangbee.utils.GeoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by test on 2019-04-15.
 */
@Service
public class WorkService {
    public static final long CLIENT_WORK_WAITHOUR = 2;
    @Autowired private FirmService firmService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired private WorkRepository repository;
    @Autowired private WorkApplicantRepository applicantRepository;
    @Autowired
    AdWithdrawHistRepository withHistRepository;
    @Autowired
    JangbeeNoticeService jangbeeNoticeService;

    @Autowired
    ExpoNotificationService expoNotificationService;

    public Work create(WorkDto.Create create) {
        String accountId = create.getAccountId();
        create.setAccountId(null);
        Work newWork = this.modelMapper.map(create, Work.class);

        // 차주일감의 최대보장시간 설정
        if (create.isFirmRegister()) {
            int guanteeTime = create.getGuaranteeTime();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime guaranteeMinute = now.plusMinutes(guanteeTime);
            newWork.setGuaranteeTime(Date.from(guaranteeMinute.atZone(ZoneId.systemDefault()).toInstant()));
        }

        newWork.setAccountId(accountId);
        newWork.setAddressPoint(GeoUtils.getPoint(create.getAddrLatitude(), create.getAddrLongitude()));
        newWork.calEndDate();
        newWork.setWorkState(WorkState.OPEN);

        Work regWork =  repository.save(newWork);

        if(regWork != null){
            // notice to those euqipment
            List<Firm> avaiFirmList =  firmService.findAvaWorkFirm(regWork.getEquipment(), regWork.getSidoAddr(), regWork.getSigunguAddr());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List tokenList = new ArrayList();

                    for(Firm firm: avaiFirmList){
                        DataSnapshot dSnapshot  = dataSnapshot.child(firm.getAccountId());
                        try {
                            AccountDto.FirebaseUser user = dSnapshot.getValue(AccountDto.FirebaseUser.class); // for(DataSnapshot ds : dataSnapshot.getChildren()) {} .child("Address")
                            if(user.getExpoPushToken() != null) {
                                tokenList.add(user.getExpoPushToken());
                            }
                        }catch (DatabaseException e) {
                            e.printStackTrace();
                        }
                    }


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String limitStr = "";
                    if (regWork.getModelYearLimit() != null ) {limitStr = regWork.getModelYearLimit()+"년식이상, ";}
                    if (regWork.getLicenseLimit() != null && !regWork.getLicenseLimit().isEmpty()) {limitStr += regWork.getLicenseLimit()+"필요, ";}
                    if (regWork.getNondestLimit() != null) {limitStr += "비파괴검사 "+regWork.getNondestLimit()+"개월이하, ";}
                    if (regWork.getCareerLimit() != null) {limitStr += regWork.getCareerLimit()+"년경력이상, ";}

                    if (!limitStr.isEmpty()) {limitStr = "["+limitStr.substring(0, limitStr.length()-2)+"]\n\n";}

                    expoNotificationService.sendMulti(tokenList, "[ "+regWork.getEquipment()+" ] 일감 올라옴", limitStr+"#배차기간:\n"+"  "+dateFormat.format(regWork.getStartDate())+" ~ "+dateFormat.format(regWork.getEndDate())+"("+regWork.getPeriodStr()+")\n\n#배차장소:\n"+"  "+regWork.getSidoAddr()+" "+regWork.getSigunguAddr(), ExpoNotiData.NOTI_WORK_REGISTER);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
        }

        return regWork;
    }

    public List<Work> getOpenFirmWorkList(String equipment, String accountId) {
        return repository.getOpenFirmWorkList(equipment, accountId, WorkState.OPEN, WorkState.SELECTED);
    }


    public List<Work> getOpenClientWorkList(String accountId) {
        return repository.getOpenClientWorkList(accountId, WorkState.SELECTED);
    }


    public List<Work> getMatchedClientWorkList(String accountId) {
        return repository.getMatchedClientWorkList(accountId, WorkState.MATCHED);
    }

    public List<Work> getMatchedFirmWorkList(String equipment, String accountId) {
        return repository.getMatchedFirmWorkList(equipment, accountId, WorkState.MATCHED);
    }

    public Work applyWork(WorkDto.Apply apply) {
        WorkApplicant applicant = new WorkApplicant();
        applicant.setWorkId(apply.getWorkId());
        applicant.setAccountId(apply.getAccountId());

        applicantRepository.save(applicant);

        Work work = repository.findOne(apply.getWorkId());
        Date applyNoticeTime = work.getApplyNoticeTime();

        Date beforeTwenty = new Date();
        beforeTwenty.setTime(beforeTwenty.getTime() - (20*60*1000));
        if(applyNoticeTime == null || beforeTwenty.after(applyNoticeTime)) {
            // Update notice Time
            repository.updateApplyNoticeTime(new Date(), work.getId());

            jangbeeNoticeService.noticeCommonMSG(work.getAccountId(), "일감 지원자 추가됨", "등록하신 일감에 신규 지원자가 있습니다, 확인 후 지원자 선택을 해 주세요.", ExpoNotiData.NOTI_WORK_ADD_REGISTER);
        }

        return work;
    }

    public WorkApplicant applyFirmWork(WorkDto.Apply apply) {
        WorkApplicant applicant = new WorkApplicant();
        applicant.setWorkId(apply.getWorkId());
        applicant.setAccountId(apply.getAccountId());
        applicant.setFirmWorkId(apply.getWorkId());

        try {
            return applicantRepository.save(applicant);
        }catch (org.springframework.dao.DataIntegrityViolationException e){
            throw new DuplicateWorkApplicantException();
        }
    }

    public void cancelApplyFirmWork(WorkApplicant applicant) {
        applicantRepository.delete(applicant.getId());
    }

    public List<Long> getApplicantWorkIdList(String accountId) {
        return applicantRepository.getWorkIdList(accountId);
    }

    public Long getApplicantCount(Long id) {
        return applicantRepository.countByWorkId(id);
    }

    public boolean selectedFirm(WorkDto.Select select) {
        Work work = repository.findOne(select.getWorkId());

        if (work != null) {
            work.setMatchedAccId(select.getAccountId());
            work.setWorkState(WorkState.SELECTED);
            work.setSelectNoticeTime(new Date());

            repository.saveAndFlush(work);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime afterThreeHour = now.plusHours(CLIENT_WORK_WAITHOUR);
            jangbeeNoticeService.noticeCommonMSG(select.getAccountId(), "배차 요청함", "지원하신 일감에 배차가 요청되었습니다, 수락/거절해 주세요(무응답시, "+afterThreeHour.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+" 이후로 취소될 수 있음)", ExpoNotiData.NOTI_WORK_SELECTED);
            return true;
        }

        return false;
    }

    public boolean acceptWork(WorkDto.Accept accept) {
        Work work = repository.findOne(accept.getWorkId());

        if (work != null && work.getWorkState().equals(WorkState.SELECTED)) {
            work.setWorkState(WorkState.MATCHED);
            repository.saveAndFlush(work);

            jangbeeNoticeService.noticeCommonMSG(work.getAccountId(), "배차됨", "장비업체에서 곧 전화가 갈 것입니다", ExpoNotiData.NOTI_WORK_ACCEPT);
            return true;
        }

        return false;
    }


    public void acceptFirmWork(Work work, WorkApplicant newAppicant) {
        if (work != null && work.getWorkState().equals(WorkState.OPEN)) {
            work.setWorkState(WorkState.MATCHED);
            work.setMatchedAccId(newAppicant.getAccountId());
            repository.saveAndFlush(work);

            jangbeeNoticeService.noticeCommonMSG(work.getAccountId(), "배차됨", "매칭된 장비업체로부터 곧 전화가 갈 것입니다", ExpoNotiData.NOTI_WORK_ACCEPT);
        }
    }

    public boolean abandonWork(WorkDto.Abandon abandon) {
        Work work = repository.findOne(abandon.getWorkId());

        if (work != null && work.getWorkState().equals(WorkState.SELECTED) && work.getMatchedAccId().equals(abandon.getMatchedAccId())) {
            work.setWorkState(WorkState.OPEN);
            work.setMatchedAccId(null);
            repository.saveAndFlush(work);

            jangbeeNoticeService.noticeCommonMSG(work.getAccountId(), "장비업체 배차포기", "아쉽게도 선택하신 업체가 사정상 배차를 포기했습니다, 다른 지원자를 다시 선택해 주세요(지원자 발생 후, 배차요청을 바로하시면 업체가 배차포기를 할 확률을 줄일 수 있습니다)", ExpoNotiData.NOTI_WORK_ABANDON);

            applicantRepository.deleteByWorkIdAndAccountId(abandon.getWorkId(), abandon.getMatchedAccId());

            return true;
        }

        return false;
    }

    public boolean cancelSelectedFirm(Long workId) {
        Work work = repository.findOne(workId);

        if (work != null) {
            String accountId = work.getMatchedAccId();

            work.setWorkState(WorkState.OPEN);
            work.setMatchedAccId(null);
            work.setSelectNoticeTime(null);

            applicantRepository.deleteByWorkIdAndAccountId(work.getId(), accountId);
            repository.saveAndFlush(work);
            return true;
        }

        return false;
    }

    public boolean updateWork(WorkDto.Update update) {
        Work work = repository.findOne(update.getWorkId());

        if (work != null) {
            work.setPhoneNumber(update.getPhoneNumber());
            work.setDetailRequest(update.getDetailRequest());

            repository.saveAndFlush(work);
            return true;
        }
        return false;
    }

    public void deleteByAccountId(String accountId) {
        applicantRepository.deleteByAccountId(accountId);

        repository.deleteByAccountId(accountId);
    }
}
