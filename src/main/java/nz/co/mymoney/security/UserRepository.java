package nz.co.mymoney.security;


import nz.co.mymoney.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserName(String userName);
    User findUserByEmail(String email);

}
