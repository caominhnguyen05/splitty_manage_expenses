package server.api;

import commons.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Main;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /**
     * Checks if the provided password equals the server-password
     * @param password input password
     * @return returns either a 200 (OK) or a 401 (UNAUTHORIZED) response
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Event> authenticate(@RequestBody String password) {
        System.out.println("Admin authentication request received");

        String serverPassword = Main.getAdmin().getPassword();

        if (serverPassword.equals(password)){
            return ResponseEntity.status(200).build();
        }
        else {
            return ResponseEntity.status(401).build();
        }
    }
}
