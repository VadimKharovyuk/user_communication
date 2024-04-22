package com.example.user_communication.Controller;

import com.example.user_communication.Repository.UserRepository;
import com.example.user_communication.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    // Создание пользователя (обрабатывает запрос POST, возвращает JSON)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userRepository.save(user);
        return ResponseEntity.ok(createdUser);
    }

    // Форма для создания нового пользователя (возвращает HTML)
    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User()); // Передаем пустой объект User
        return "create-user"; // Имя HTML-шаблона для отображения формы
    }

    // Сохранение нового пользователя (POST-запрос, перенаправление)
    @PostMapping("/create")
    public String createUserForm(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/users/all"; // Перенаправление после успешного сохранения
    }

    // Получение всех пользователей для отображения в виде HTML
    @GetMapping("/all")
    public String listAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "list-users"; // Имя шаблона Thymeleaf для отображения всех пользователей
    }

    // Получение пользователя по идентификатору
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    // Обновление пользователя
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPassword(user.getPassword());
                    userRepository.save(existingUser);
                    return ResponseEntity.ok(existingUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Удаление пользователя
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        return userRepository.findById(id)
//                .map(existingUser -> {
//                    userRepository.delete(existingUser);
//                    return ResponseEntity.noContent().build(); // Возвращаемый тип Void
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
}
