package yang.lin35.networking_service_demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import yang.lin35.networking_service_demo.dao.date_entitys.User;

@Mapper
public interface UserMapper {
	@Select("SELECT id,name as username,contact_list as contactList,password FROM USER WHERE NAME = #{username}")
	User findUserByUsername(@Param("username") String username);

	@Select("SELECT id,name as username,contact_list as contactList,password FROM USER WHERE id = #{id}")
	User findUserById(@Param("id")Long id);
}
