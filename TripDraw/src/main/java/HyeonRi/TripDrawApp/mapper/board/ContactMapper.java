package HyeonRi.TripDrawApp.mapper.board;

import java.util.List;
import org.apache.ibatis.annotations.*;

import HyeonRi.TripDrawApp.dto.board.contact.ContactDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactCommentDto;
import HyeonRi.TripDrawApp.dto.board.contact.ContactImageDto;

@Mapper
public interface ContactMapper {

    // ==== Contact CRUD ====  

    @Insert("""
        INSERT INTO contact (user_id, title, content, is_secret, view_count, created_at)
        VALUES (#{userId}, #{title}, #{content}, #{isSecret}, #{viewCount}, #{createdAt})
    """ )
    @Options(useGeneratedKeys = true, keyProperty = "contactId")
    void insertContact(ContactDto dto);

    @Select("SELECT * FROM contact WHERE contact_id = #{contactId}")
    ContactDto selectContactById(Long contactId);

    @Select("SELECT * FROM contact ORDER BY created_at DESC")
    List<ContactDto> selectAllContacts();

    @Update("""
        UPDATE contact
           SET title     = #{title},
               content   = #{content},
               is_secret = #{isSecret}
         WHERE contact_id = #{contactId}
    """ )
    void updateContact(ContactDto dto);

    @Delete("DELETE FROM contact WHERE contact_id = #{contactId}")
    void deleteContact(Long contactId);

    // ==== Comment CRUD ====  

    @Insert("""
        INSERT INTO contact_comment (contact_id, user_id, content, created_at)
        VALUES (#{contactId}, #{userId}, #{content}, #{createdAt})
    """ )
    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    void insertComment(ContactCommentDto dto);

    @Select("""
        SELECT * 
          FROM contact_comment
         WHERE contact_id = #{contactId}
         ORDER BY created_at ASC
    """ )
    List<ContactCommentDto> selectCommentsByContactId(Long contactId);

    @Update("""
        UPDATE contact_comment
           SET content = #{content}
         WHERE contact_id = #{contactId}
           AND comment_id = #{commentId}
    """ )
    void updateComment(ContactCommentDto dto);

    @Delete("""
        DELETE FROM contact_comment 
         WHERE contact_id = #{contactId}
           AND comment_id = #{commentId}
    """
    )
    void deleteComment(@Param("contactId") Long contactId,
                       @Param("commentId") Long commentId);

    // ==== Image CRUD ====  

    @Insert("""
        INSERT INTO contact_image (contact_id, image_url)
        VALUES (#{contactId}, #{imageUrl})
    """ )
    @Options(useGeneratedKeys = true, keyProperty = "imageId")
    void insertImage(ContactImageDto dto);

    @Select("SELECT * FROM contact_image WHERE contact_id = #{contactId}")
    List<ContactImageDto> selectImagesByContactId(Long contactId);

    @Delete("""
        DELETE FROM contact_image
         WHERE contact_id = #{contactId}
           AND image_id   = #{imageId}
    """ )
    void deleteImage(@Param("contactId") Long contactId,
                     @Param("imageId")    Long imageId);
}