package HyeonRi.TripDrawApp.service.board;

import HyeonRi.TripDrawApp.dto.board.contact.ContactDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactCommentDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactImageDto;
import HyeonRi.TripDrawApp.mapper.board.ContactMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMapper contactMapper;

    // ---------------------- Contact ----------------------

    public int createContact(ContactDto dto) {
        return contactMapper.insertContact(dto);
    }

    public ContactDto getContact(Integer contactId) {
        return contactMapper.selectContact(contactId);
    }

    public List<ContactDto> getAllContacts() {
        return contactMapper.selectAllContacts();
    }

    public int updateContact(ContactDto dto) {
        return contactMapper.updateContact(dto);
    }

    public int deleteContact(Integer contactId) {
        return contactMapper.deleteContact(contactId);
    }

    // ---------------------- Contact Comment ----------------------

    public int addComment(ContactCommentDto dto) {
        return contactMapper.insertComment(dto);
    }

    public List<ContactCommentDto> getComments(Integer contactId) {
        return contactMapper.selectCommentsByContactId(contactId);
    }

    public int deleteComment(Integer commentId) {
        return contactMapper.deleteComment(commentId);
    }

    // ---------------------- Contact Image ----------------------

    public int addImage(ContactImageDto dto) {
        return contactMapper.insertImage(dto);
    }

    public List<ContactImageDto> getImages(Integer contactId) {
        return contactMapper.selectImagesByContactId(contactId);
    }

    public int deleteImage(Integer imageId) {
        return contactMapper.deleteImage(imageId);
    }
}
