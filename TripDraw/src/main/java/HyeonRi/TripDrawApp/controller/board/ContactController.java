package HyeonRi.TripDrawApp.controller.board;

import HyeonRi.TripDrawApp.dto.board.contact.ContactDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactCommentDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactImageDto;
import HyeonRi.TripDrawApp.service.board.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    // ---------------------- Contact ----------------------

    @PostMapping
    public ResponseEntity<Void> createContact(@RequestBody ContactDto dto) {
        contactService.createContact(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<ContactDto> getContact(@PathVariable Integer contactId) {
        return ResponseEntity.ok(contactService.getContact(contactId));
    }

    @GetMapping
    public ResponseEntity<List<ContactDto>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<Void> updateContact(@PathVariable Integer contactId, @RequestBody ContactDto dto) {
        dto.setContactId(contactId);
        contactService.updateContact(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Integer contactId) {
        contactService.deleteContact(contactId);
        return ResponseEntity.ok().build();
    }

    // ---------------------- Comment ----------------------

    @PostMapping("/{contactId}/comment")
    public ResponseEntity<Void> addComment(@PathVariable Integer contactId, @RequestBody ContactCommentDto dto) {
        dto.setContactId(contactId);
        contactService.addComment(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{contactId}/comment")
    public ResponseEntity<List<ContactCommentDto>> getComments(@PathVariable Integer contactId) {
        return ResponseEntity.ok(contactService.getComments(contactId));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        contactService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    // ---------------------- Image ----------------------

    @PostMapping("/{contactId}/image")
    public ResponseEntity<Void> addImage(@PathVariable Integer contactId, @RequestBody ContactImageDto dto) {
        dto.setContactId(contactId);
        contactService.addImage(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{contactId}/image")
    public ResponseEntity<List<ContactImageDto>> getImages(@PathVariable Integer contactId) {
        return ResponseEntity.ok(contactService.getImages(contactId));
    }

    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer imageId) {
        contactService.deleteImage(imageId);
        return ResponseEntity.ok().build();
    }
}
