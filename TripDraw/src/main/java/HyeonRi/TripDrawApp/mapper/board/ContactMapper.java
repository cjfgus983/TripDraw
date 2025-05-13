package HyeonRi.TripDrawApp.mapper.board;

import HyeonRi.TripDrawApp.dto.board.contact.ContactDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactCommentDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactImageDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContactMapper {

    // ---------------------- Contact ----------------------

    @Insert("""
        INSERT INTO contact (user_id, title, content, is_secret, view_count, created_at)
        VALUES (#{userId}, #{title}, #{content}, #{isSecret}, #{viewCount}, #{createdAt})
        """)
    int insertContact(ContactDto dto);

    @Select("SELECT * FROM contact WHERE contact_id = #{id}")
    ContactDto selectContact(Integer id);

    @Select("SELECT * FROM contact ORDER BY created_at DESC")
    List<ContactDto> selectAllContacts();

    @Update("""
        UPDATE contact
        SET title = #{title}, content = #{content}, is_secret = #{isSecret}
        WHERE contact_id = #{contactId}
        """)
    int updateContact(ContactDto dto);

    @Delete("DELETE FROM contact WHERE contact_id = #{id}")
    int deleteContact(Integer id);


    // ---------------------- Contact Comment ----------------------

    @Insert("""
        INSERT INTO contact_comment (contact_id, user_id, content, created_at)
        VALUES (#{contactId}, #{userId}, #{content}, #{createdAt})
        """)
    int insertComment(ContactCommentDto dto);

    @Select("SELECT * FROM contact_comment WHERE contact_id = #{contactId}")
    List<ContactCommentDto> selectCommentsByContactId(Integer contactId);

    @Delete("DELETE FROM contact_comment WHERE comment_id = #{commentId}")
    int deleteComment(Integer commentId);


    // ---------------------- Contact Image ----------------------

    @Insert("""
        INSERT INTO contact_image (contact_id, image_url)
        VALUES (#{contactId}, #{imageUrl})
        """)
    int insertImage(ContactImageDto dto);

    @Select("SELECT * FROM contact_image WHERE contact_id = #{contactId}")
    List<ContactImageDto> selectImagesByContactId(Integer contactId);

    @Delete("DELETE FROM contact_image WHERE image_id = #{imageId}")
    int deleteImage(Integer imageId);
}
