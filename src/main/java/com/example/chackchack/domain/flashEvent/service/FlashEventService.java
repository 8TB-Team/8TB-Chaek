package com.example.chackchack.domain.flashEvent.service;

import com.example.chackchack.domain.flashEvent.dto.request.EventCreateRequest;
import com.example.chackchack.domain.flashEvent.entity.EventMap;
import com.example.chackchack.domain.flashEvent.entity.EventUserMap;
import com.example.chackchack.domain.flashEvent.repository.EventMapRepository;
import com.example.chackchack.domain.flashEvent.repository.EventUserMapRepository;
import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.service.UserExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlashEventService {

    private final EventMapRepository eventMapRepository;
    private final EventUserMapRepository eventUserMapRepository;

    private final UserExternalService userExternalService;

    @Transactional
    public void createNewEvent(EventCreateRequest request) {
        EventMap event = EventMap.from(request);
        eventMapRepository.save(event);
    }

    @Transactional
    public void registerUserToEvent(Long eventId, Long userId) {

        EventMap event = findEventMapOrElseThrow(eventId);
        int currentParticipants = eventUserMapRepository.countByEventMap(event);

        if (currentParticipants >= event.getMaxParticipants())
            // TODO 예외 처리
            throw new IllegalStateException("참여자 수가 최대 인원에 도달했습니다.");

        User user = userExternalService.findUserByIdOrElseThrow(userId);
        EventUserMap registerUser = EventUserMap.of(user, event);

        eventUserMapRepository.save(registerUser);
    }

    public EventMap findEventMapOrElseThrow(Long eventId) {

        // TODO 예외 처리
        return eventMapRepository.findByIdWithDistributedLock(eventId).orElseThrow(RuntimeException::new);
    }
}
