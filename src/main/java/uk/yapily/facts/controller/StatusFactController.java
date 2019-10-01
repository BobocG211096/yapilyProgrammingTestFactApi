package uk.yapily.facts.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.yapily.facts.model.StatusFactResponse;
import uk.yapily.facts.service.StatusFactService;

@RestController
public class StatusFactController {
    private StatusFactService statusFactService;

    @Autowired
    public StatusFactController(StatusFactService statusFactService){
        this.statusFactService = statusFactService;
    }

    @ApiOperation("Retrieve the status of the server")
    @GetMapping("/status")
    public StatusFactResponse getStatus(){
        return statusFactService.retrieveStatusFactResponse();
    }
}
