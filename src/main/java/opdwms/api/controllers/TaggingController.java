package opdwms.api.controllers;

import opdwms.api.models.TagHotlist;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TaggingController {

    @PostMapping(path = "/api/tag-hot-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, Object> processWeighingRequestFromWeighbridgeStations(@RequestBody TagHotlist request) {

        return null;
    }

}
