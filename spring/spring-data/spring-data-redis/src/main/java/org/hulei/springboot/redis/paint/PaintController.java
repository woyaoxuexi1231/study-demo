package org.hulei.springboot.redis.paint;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hulei.util.dto.ResultDTO;
import org.hulei.util.utils.ResultDTOBuild;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hulei
 * @since 2024/12/12 23:31
 */

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/paint")
@RestController
public class PaintController {

    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping(value = "/draw")
    public void draw(@RequestBody DrawCoordinatesDTO req) {
        redisTemplate.opsForHash().put("paint:draw", String.format("row:%d:col:%d", req.getRow(), req.getCol()), req.getColor());
    }

    @GetMapping(value = "/get-colors")
    public ResultDTO<List<DrawCoordinatesDTO>> getColors() {

        Map<Object, Object> entries = redisTemplate.opsForHash().entries("paint:draw");

        List<DrawCoordinatesDTO> colors = new ArrayList<>();
        entries.forEach((k, v) -> {
            String[] split = ((String) k).split(":");
            colors.add(new DrawCoordinatesDTO()
                    .setRow(Integer.parseInt(split[1]))
                    .setCol(Integer.parseInt(split[3]))
                    .setColor((String) v)
            );
        });

        return ResultDTOBuild.resultSuccessBuild(colors);
    }
}
