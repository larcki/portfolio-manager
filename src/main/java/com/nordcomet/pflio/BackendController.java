package com.nordcomet.pflio;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackendController {

    @RequestMapping("/api/chart")
    public String chart() {
        return "This is coming from backend!!";
    }


}
