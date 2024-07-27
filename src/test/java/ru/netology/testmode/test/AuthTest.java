package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static ru.netology.testmode.data.DataGenerator.*;

public class AuthTest {

    @BeforeEach
    void setup() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    @DisplayName("Тест, проверяющий доступ к личному кабинету для активного зарегистированного пользователя")
    void shouldSuccessfullyLoginWithActiveRegisteredUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2")
                .shouldHave(Condition.exactText("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Если пользователь не зарегистрирован появляется сообщение об ошибке")
    void shouldGetErrorMessageInRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Появляется сообщение об ошибке, если зарегистированный пользователь заблокирован")
    void shouldGetErrorMessageIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Появляется сообщение об ошибке, если ввести невалидный логин и валидный пароль")
    void shouldGetErrorMessageInWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Появляется сообщение об ошибке, если ввести валидный логин и не валидный пароль")
    void shouldGetErrorMessageInWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    //Время рассчитано на 1 тест с учетом старой техники, на которой прогоняются тесты
    //время, затраченное на ручное тестирование (минут): 2 минуты;
    //время, затраченное на автоматизацию (минут): 1 минута.
}
