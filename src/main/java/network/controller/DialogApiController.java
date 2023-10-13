package network.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import network.dto.DialogueMessage;
import network.dto.NewMessageModel;
import network.service.DialogueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class DialogApiController implements DialogApi {
    private final DialogueService dialogueService;

    public DialogApiController(DialogueService dialogueService) {
        this.dialogueService = dialogueService;
    }

    public ResponseEntity<List<DialogueMessage>> dialogUserIdListGet(@Parameter(in = ParameterIn.PATH, required = true, schema = @Schema()) @PathVariable("user_id") String userId, Principal principal) {
        return new ResponseEntity<>(dialogueService.getAllMessages(principal.getName(), userId), HttpStatus.OK);
    }

    public ResponseEntity<Void> dialogUserIdSendPost(@Parameter(in = ParameterIn.PATH, required = true, schema = @Schema()) @PathVariable("user_id") String userId, @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @Valid @RequestBody NewMessageModel body, Principal principal) {
        dialogueService.createMessage(principal.getName(), userId, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
