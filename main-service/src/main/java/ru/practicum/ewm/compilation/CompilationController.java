package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationController {
    private final CompilationService compilationService;

    /**
     * Получение подборки событий
     *
     * @param pinned Закрепленные или не закрепленные подборки
     * @param from   с какого элемента начать
     * @param size   количество на странице
     * @return Подборки событий
     */
    @GetMapping("/compilations")
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilation(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Вывод подборки событий, pinned={}, size={}, from={}", pinned, size, from);
        return compilationService.getCompilation(pinned, from, size);
    }

    /**
     * Получение подборки событий по ID
     *
     * @param compId ID
     * @return Подборка событий
     */
    @GetMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.info("Вывод подборки событий по ID={}", compId);
        return compilationService.getCompilationById(compId);
    }

    /**
     * Создание подборки событий
     *
     * @param newCompilationDto Данные подборки событий
     * @return Созданная подборка событий
     */
    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Создание подборки событий {}", newCompilationDto);
        return compilationService.postCompilation(newCompilationDto);
    }

    /**
     * Удаление подборки событий по ID
     *
     * @param compId ID подборки событий
     */
    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable long compId) {
        log.info("Удаление подборки событий по ID={}", compId);
        compilationService.deleteCompilationById(compId);
    }

    /**
     * Обновление подборки событий
     *
     * @param compId                   ID подборки событий
     * @param updateCompilationRequest Данные на обновление
     * @return Обновленная подборка событий
     */
    @PatchMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto patchCompilation(@PathVariable long compId,
                                           @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Обновление подборки событий по ID={}, данные на обновление {}", compId, updateCompilationRequest);
        return compilationService.patchCompilation(compId, updateCompilationRequest);
    }
}
