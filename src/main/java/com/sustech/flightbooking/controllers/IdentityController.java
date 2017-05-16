package com.sustech.flightbooking.controllers;

import com.sustech.flightbooking.domainmodel.Flight;
import com.sustech.flightbooking.domainmodel.FlightStatus;
import com.sustech.flightbooking.domainmodel.Passenger;
import com.sustech.flightbooking.infrastructure.FlightBookingAuthenticationToken;
import com.sustech.flightbooking.misc.SearchInfos;
import com.sustech.flightbooking.misc.responseHandling.ErrorMessageHandler;
import com.sustech.flightbooking.persistence.PassengerRepository;
import com.sustech.flightbooking.services.FlightService;
import com.sustech.flightbooking.services.IdentityService;
import com.sustech.flightbooking.services.UserService;
import com.sustech.flightbooking.viewmodel.LoginViewModel;
import com.sustech.flightbooking.viewmodel.PassengerEditModelViewModel;
import com.sustech.flightbooking.viewmodel.ViewModelValidator;
import com.sustech.flightbooking.viewmodel.passenger.flight.AvailableFlightListViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class IdentityController extends ControllerBase {

    private final IdentityService identityService;
    private final PassengerRepository passengerRepository;
    private final FlightService flightService;
    private final UserService userService;


    @Autowired
    public IdentityController(IdentityService identityService, PassengerRepository passengerRepository,
                              FlightService flightService,
                              UserService userService) {
        this.identityService = identityService;
        this.passengerRepository = passengerRepository;
        this.flightService = flightService;
        this.userService = userService;
    }


    @GetMapping("/login")
    public ModelAndView loginPage(@RequestParam(value = "returnUri", required = false) String returnUri) {
        LoginViewModel viewModel = new LoginViewModel();
        viewModel.setReturnUri(returnUri);
        if (identityService.getCurrentUser() != null && !returnUri.isEmpty()) {
            return loginPageWithErrorMessages(viewModel, errorMessages("You may not have permission accessing that page."));
        }
        return pageWithViewModel("login", viewModel);
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute LoginViewModel model) {
        FlightBookingAuthenticationToken token = identityService.login(model.getUserName(), model.getPassword());
        if (token != null) {
            String returnUri = model.getReturnUri().isEmpty() ?
                    ("/" + (token.getRole().equalsIgnoreCase("passenger") ? "passenger" : "manage"))
                    : model.getReturnUri();
            return redirect(returnUri);
        }
        //clear password
        model.setPassword("");
        return loginPageWithErrorMessages(model, errorMessages("Invalid user name or password"));
    }

    @GetMapping("/logout")
    public ModelAndView logout() {
        identityService.logout();
        return redirect("/");
    }

    @GetMapping("register")
    public ModelAndView registerPage() {
        return pageWithViewModel("register",
                new PassengerEditModelViewModel());
    }

    @PostMapping("register")
    public ModelAndView register(@ModelAttribute PassengerEditModelViewModel model) {
        List<String> errorMessages = ViewModelValidator.validate(model);
        if (userService.isUserNameRegisteredFor(null, model.getUserName())) {
            errorMessages.add("User name already exists.");
        }
        if (userService.isIdCardRegisteredFor(null, model.getIdentityNumber())) {
            errorMessages.add("ID card is already registered.");
        }
        return ErrorMessageHandler.fromViewModel(model, "register")
                .addErrorMessages(errorMessages)
                .onSuccess(() -> {
                    Passenger passenger = new Passenger(UUID.randomUUID());

                    passenger.setUserName(model.getUserName());
                    passenger.setDisplayName(model.getDisplayName());
                    passenger.setIdentityCardNumber(model.getIdentityNumber());
                    passenger.setPassword(model.getPassword());

                    passengerRepository.save(passenger);
                    return redirect("/login");
                })
                .result();
    }


    private ModelAndView loginPageWithErrorMessages(Object viewModel, List<String> errorMessages) {
        ModelAndView modelAndView = pageWithViewModel("login", viewModel);
        modelAndView.getModelMap().put("errorMessages", errorMessages);
        return modelAndView;
    }
}
