package healthcare.api.service;

import healthcare.api.data.ConditionBase;
import healthcare.api.data.FilterReq;
import healthcare.api.data.OperatorBase;
import healthcare.api.data.RequestParams;
import healthcare.entity.*;
import healthcare.entity.dto.req.DiaryReq;
import healthcare.entity.dto.req.SoundReq;
import healthcare.entity.dto.resp.DiaryResp;
import healthcare.entity.dto.resp.SoundResp;
import healthcare.entity.enums.MoodStatus;
import healthcare.entity.enums.Role;
import healthcare.repository.MoodSoundRepository;
import healthcare.repository.SoundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SoundService extends BaseService<Sound> {

    @Override
    protected SoundRepository getRepository() {
        return repository;
    }

    @Override
    protected Class<Sound> getType() {
        return Sound.class;
    }
    final SoundRepository repository;
    final AccountService accountService;
    final MoodSoundRepository moodSoundRepository;
    final DropboxService dropboxService;
    final MoodSoundService moodSoundService;
    /*public SoundResp create(SoundReq req) throws Exception {
        UserPrin user = accountService.checkUserPermission(Role.ADMIN.name());
        MoodSound moodSound = moodSoundRepository.getById(req.getMoodId());
        if (!moodSound.getStatus().equals(MoodStatus.ACTIVE)) {
            throw new Exception("moodSound-not-active");
        }
        Sound sound = this.reqToEntity(req,new Sound());
        sound.setCreatedBy(user.getId());
        return this.entityToResp(this.save(sound),SoundResp.class);
    }*/
    public SoundResp create(SoundReq req) throws Exception {
        UserPrin user = accountService.getCurrentUser();
        MoodSound moodSound = moodSoundService.getEntityById(req.getMoodSoundId());
        if (!moodSound.getStatus().equals(MoodStatus.ACTIVE)) {
            throw new Exception("moodSound-not-active");
        }
        Sound sound = this.reqToEntity(req, new Sound());
        sound.setCreatedBy(user.getId());
        sound.setMoodSound(moodSound);
        return this.entityToResp(this.save(sound), SoundResp.class);
    }

    public SoundResp update(SoundReq req, Long id) throws Exception {
        accountService.checkUserPermission(Role.ADMIN.name());
        Sound sound = this.getById(id);
        Sound sound1 = this.reqToEntity(req,sound);
        return this.entityToResp(repository.save(sound1),SoundResp.class);
    }
    public void delete(Long id){
        accountService.checkUserPermission(Role.ADMIN.name());
        this.deleteById(id);
    }
    public  List<?> getSoundByMood(long moodSoundId)throws Exception{
        accountService.getCurrentUser();
        MoodSound moodSound = moodSoundRepository.findById(moodSoundId).orElseThrow(() -> new EntityNotFoundException("MoodSound not found"));
        List<Sound> sounds = repository.findAllByMoodSound(moodSound);
        return sounds.stream().map(sound -> this.entityToResp(sound, SoundResp.class)).collect(Collectors.toList());
    }
    public List<?> getListSound(RequestParams params) throws Exception {
        accountService.getCurrentUser();
        Specification<Sound> specification = this.buildSpecification(params.getFilter());
        List<Sound> sounds = this.getAll(specification);
        return sounds.stream().map(sound -> this.entityToResp(sound, SoundResp.class)).collect(Collectors.toList());
    }
    public SoundResp findById(Long id){
        accountService.getCurrentUser();
        Sound sound = this.getById(id);
        return this.entityToResp(sound,SoundResp.class);
    }

    public Page<?> getPaginated(RequestParams requestParams) throws Exception {
        Specification<Sound> specification = this.buildSpecification(requestParams.getFilter());
        Page<Sound> sounds = this.getPaginated(specification, requestParams.getPageable());
        return sounds.map(sound -> this.entityToResp(sound, DiaryResp.class));
    }

}
