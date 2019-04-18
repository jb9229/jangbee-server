package com.jangbee.work;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jangbee.accounts.AccountDto;
import com.jangbee.expo.ExpoNotiData;
import com.jangbee.expo.ExpoNotificationService;
import com.jangbee.utils.GeoUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
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
    @Autowired ExpoNotificationService expoNotificationService;

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
            // Notice
            FirebaseDatabase.getInstance().getReference("users/" +work.getAccountId()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            AccountDto.FirebaseUser user = dataSnapshot.getValue(AccountDto.FirebaseUser.class);
                            expoNotificationService.sendSingle(user.getExpoPushToken(), "일감 지원자 추가됨", "등록하신 일감에 신규 지원자가 있습니다, 확인 후 지원자 선택을 해 주세요.", ExpoNotiData.NOTI_WORK_ADD_REGISTER);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        return work;
    }

    public List<Long> getApplicantWorkIdList(String accountId) {
        return applicantRepository.getWorkIdList(accountId);
    }

    public Long getApplicantCount(Long id) {
        return applicantRepository.countByWorkId(id);
    }

    public boolean selectedFirm(Long workId, String accountId) {
        Work work = repository.getOne(workId);

        if(work != null){
            work.setMatchedAccId(accountId);
            work.setWorkState(WorkState.SELECTED);

            return true;
        }

        return false;
    }
}
