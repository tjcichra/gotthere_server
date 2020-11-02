package com.tim.gotthere_server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
    
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private GotthereDatabase databaseController;
    
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings2")
    public LocationsResponse greeting(LocationsRequest request) throws Exception {
        //Converts date-times from YYYY-MM-DDTHH:mm to YYYY-MM-DD HH:mm
        String startDateTime = Util.javaScriptDateTimeToSQL(request.getStartDateTime());
        String endDateTime = Util.javaScriptDateTimeToSQL(request.getEndDateTime());

        List<Location> locations = this.databaseController.getLocations(startDateTime, endDateTime);
        return new LocationsResponse(locations);
    }

    @MessageMapping("/hello2")
    @SendTo("/topic/greetings3")
    public LoginResponse getLogin(LoginRequest request) throws Exception {
        return new LoginResponse(this.databaseController.validateLogin(request.getUsername(), request.getPassword()));
    }

    public void autoSendingMessage(Location location) throws Exception {
        Thread.sleep(1000); // simulated delay
        this.template.convertAndSend("/topic/greetings", location);
    }
}
