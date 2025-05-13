package HyeonRi.TripDrawApp.service.board;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HyeonRi.TripDrawApp.dto.board.contact.ContactDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactCommentDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactImageDto;
import HyeonRi.TripDrawApp.mapper.board.ContactMapper;

@Service
@Transactional
public class ContactService {

    private final ContactMapper mapper;

    public ContactService(ContactMapper mapper) {
        this.mapper = mapper;
    }

    // ===== 게시글 =====
    public Long createContact(ContactDto dto) {
        mapper.insertContact(dto);
        return dto.getContactId();
    }

    @Transactional(readOnly = true)
    public ContactDto getContact(Long contactId) {
        return mapper.selectContactById(contactId);
    }

    @Transactional(readOnly = true)
    public List<ContactDto> getAllContacts() {
        return mapper.selectAllContacts();
    }

    public void updateContact(ContactDto dto) {
        mapper.updateContact(dto);
    }

    public void deleteContact(Long contactId) {
        mapper.deleteContact(contactId);
    }

    // ===== 댓글 =====
    public void addComment(ContactCommentDto dto) {
        mapper.insertComment(dto);
    }

    @Transactional(readOnly = true)
    public List<ContactCommentDto> getComments(Long contactId) {
        return mapper.selectCommentsByContactId(contactId);
    }

    public void updateComment(ContactCommentDto dto) {
        mapper.updateComment(dto);
    }

    public void deleteComment(Long contactId, Long commentId) {
        mapper.deleteComment(contactId, commentId);
    }

    // ===== 이미지 =====
    public void addImage(ContactImageDto dto) {
        mapper.insertImage(dto);
    }

    @Transactional(readOnly = true)
    public List<ContactImageDto> getImages(Long contactId) {
        return mapper.selectImagesByContactId(contactId);
    }

    public void deleteImage(Long contactId, Long imageId) {
        mapper.deleteImage(contactId, imageId);
    }
} 
