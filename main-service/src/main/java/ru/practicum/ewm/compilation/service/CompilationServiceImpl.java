package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.exception.CompilationNotFoundException;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    /**
     * Получение подборки событий
     *
     * @param pinned Закрепленные или не закрепленные подборки
     * @param from   с какого элемента начать
     * @param size   количество на странице
     * @return Подборки событий
     */
    @Override
    public List<CompilationDto> getCompilation(Boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        if (pinned != null) {
            return CompilationMapper.toCompilationDto(compilationRepository.findAllByPinned(pinned, page));
        } else {
            return CompilationMapper.toCompilationDto(compilationRepository.findAll(page).getContent());
        }
    }

    /**
     * Получение подборки событий по ID
     *
     * @param compId ID
     * @return Подборка событий
     */
    @Override
    public CompilationDto getCompilationById(long compId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compId).orElseThrow(() ->
                new CompilationNotFoundException(compId)));
    }

    /**
     * Создание подборки событий
     *
     * @param newCompilationDto Данные подборки событий
     * @return Созданная подборка событий
     */
    @Override
    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
            return CompilationMapper.toCompilationDto(compilationRepository
                    .save(CompilationMapper.newCompilationDtoToCompilation(newCompilationDto, events)));
        } else {
            return CompilationMapper.toCompilationDto(compilationRepository
                    .save(CompilationMapper.newCompilationDtoToCompilation(newCompilationDto)));
        }
    }

    /**
     * Удаление подборки событий по ID
     *
     * @param compId ID подборки событий
     */
    @Override
    public void deleteCompilationById(long compId) {
        compilationRepository.findById(compId).orElseThrow(() ->
                new CompilationNotFoundException(compId));
        compilationRepository.deleteById(compId);
    }

    /**
     * Обновление подборки событий
     *
     * @param compId                   ID подборки событий
     * @param updateCompilationRequest Данные на обновление
     * @return Обновленная подборка событий
     */
    @Override
    public CompilationDto patchCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new CompilationNotFoundException(compId));
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }
}
