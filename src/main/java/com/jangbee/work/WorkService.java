package com.jangbee.work;

import com.jangbee.common.NoticeService;
import com.jangbee.expo.ExpoNotiData;
import com.jangbee.utils.GeoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by test on 2019-04-15.
 */
@Service
public class WorkService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired private WorkRepository repository;
    @Autowired private WorkApplicantRepository applicantRepository;
    @Autowired
    NoticeService noticeService;

    public Work create(WorkDto.Create create) {
        Work newWork = this.modelMapper.map(create, Work.class);
        newWork.setAddressPoint(GeoUtils.getPoint(create.getAddrLatitude(), create.getAddrLongitude()));

        return repository.save(newWork);
    }

    public List<Work> getOpenFirmWorkList(String equipment, String accountId) {
        return repository.getOpenFirmWorkList(equipment, accountId);
    }


    public List<Work> getOpenClientWorkList(String accountId) {
        return repository.getOpenClientWorkList(accountId);
    }

    public List<Work> getMatchedFirmWorkList(String equipment, String accountId) {
        return repository.getMatchedFirmWorkList(equipment, accountId);
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

            noticeService.noticeCommonMSG(work.getAccountId(), "일감 지원자 추가됨", "등록하신 일감에 신규 지원자가 있습니다, 확인 후 지원자 선택을 해 주세요.", ExpoNotiData.NOTI_WORK_ADD_REGISTER);
        }

        return work;
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

            LocalDateTime afterThreeHour = LocalDateTime.now();
            afterThreeHour.plusHours(3);
            noticeService.noticeCommonMSG(select.getAccountId(), "배차 요청함", "지원하신 일감에 배차가 요청되었습니다, 수락/거절해 주세요(무응답시, "+afterThreeHour.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+" 이후로 취소될 수 있음)", ExpoNotiData.NOTI_WORK_SELECTED);
            return true;
        }

        return false;
    }

    public boolean acceptWork(WorkDto.Accept accept) {
        Work work = repository.findOne(accept.getWorkId());

        if (work != null && work.getWorkState().equals(WorkState.SELECTED)) {
            work.setWorkState(WorkState.MATCHED);
            repository.saveAndFlush(work);

            noticeService.noticeCommonMSG(work.getAccountId(), "배차됨", "장비업체에서 곧 전화가 갈 것입니다", ExpoNotiData.NOTI_WORK_ACCEPT);
            return true;
        }
        return false;
    }
}
