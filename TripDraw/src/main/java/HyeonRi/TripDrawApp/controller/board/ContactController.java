package HyeonRi.TripDrawApp.controller.board;


import HyeonRi.TripDrawApp.dto.board.contact.ContactCommentDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactImageDto;
import HyeonRi.TripDrawApp.service.board.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")

public class ContactController {

    private final ContactService contactService;


    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // ===== Contact =====

    @PostMapping
    public ResponseEntity<Long> createContact(@RequestBody ContactDto dto) {
        return ResponseEntity.ok(contactService.createContact(dto));
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<ContactDto> getContact(@PathVariable Long contactId) {
        return ResponseEntity.ok(contactService.getContact(contactId));
    }

    @GetMapping
    public ResponseEntity<List<ContactDto>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @PutMapping("/{contactId}")

    public ResponseEntity<Void> updateContact(@PathVariable Long contactId, @RequestBody ContactDto dto) {
        dto.setContactId(contactId);
        contactService.updateContact(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        contactService.deleteContact(contactId);
        return ResponseEntity.ok().build();
    }
    // ===== Comment =====

    @PostMapping("/{contactId}/comment")
    public ResponseEntity<Void> addComment(@PathVariable Long contactId, @RequestBody ContactCommentDto dto) {
        dto.setContactId(contactId);
        contactService.addComment(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{contactId}/comment")
    public ResponseEntity<List<ContactCommentDto>> getComments(@PathVariable Long contactId) {
        return ResponseEntity.ok(contactService.getComments(contactId));
    }

    @PutMapping("/{contactId}/comment/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long contactId,
                                              @PathVariable Long commentId,
                                              @RequestBody ContactCommentDto dto) {
        dto.setContactId(contactId);
        dto.setCommentId(commentId);
        contactService.updateComment(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{contactId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long contactId, @PathVariable Long commentId) {
        contactService.deleteComment(contactId, commentId);
        return ResponseEntity.ok().build();
    }

    // ===== Image =====

    @PostMapping("/{contactId}/image")
    public ResponseEntity<Void> addImage(@PathVariable Long contactId, @RequestBody ContactImageDto dto) {
        dto.setContactId(contactId);
        contactService.addImage(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{contactId}/image")
    public ResponseEntity<List<ContactImageDto>> getImages(@PathVariable Long contactId) {
        return ResponseEntity.ok(contactService.getImages(contactId));
    }

    @DeleteMapping("/{contactId}/image/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long contactId, @PathVariable Long imageId) {
        contactService.deleteImage(contactId, imageId);
        return ResponseEntity.ok().build();
    }
}
