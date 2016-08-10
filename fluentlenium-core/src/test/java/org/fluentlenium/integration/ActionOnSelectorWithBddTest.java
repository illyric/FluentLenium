package org.fluentlenium.integration;

import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.integration.localtest.LocalFluentCase;
import org.junit.Test;
import org.openqa.selenium.support.ui.Select;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionOnSelectorWithBddTest extends LocalFluentCase {
    @Test
    public void checkFillAction() {
        goTo(DEFAULT_URL);
        assertThat(findFirst("#name").getValue()).contains("John");
        findFirst("#name").fill().with("zzz");
        assertThat(findFirst("#name").getValue()).isEqualTo("zzz");
    }

    @Test
    public void checkFillSelectAction() {
        goTo(DEFAULT_URL);
        Select select = new Select(findFirst("#select").getElement());
        $("#select").fillSelect().withValue("value-1"); // by value
        assertThat(select.getFirstSelectedOption().getText()).isEqualTo("value 1");
        $("#select").fillSelect().withIndex(1); // by index
        assertThat(select.getFirstSelectedOption().getText()).isEqualTo("value 2");
        $("#select").fillSelect().withText("value 3"); // by text
        assertThat(select.getFirstSelectedOption().getText()).isEqualTo("value 3");
    }

    @Test
    public void checkFillSelectActionOnSelectElement() {
        goTo(DEFAULT_URL);
        FluentWebElement element = findFirst("#select");
        Select select = new Select(element.getElement());
        element.fillSelect().withValue("value-1"); // by value
        assertThat(select.getFirstSelectedOption().getText()).isEqualTo("value 1");
        element.fillSelect().withIndex(1); // by index
        assertThat(select.getFirstSelectedOption().getText()).isEqualTo("value 2");
        element.fillSelect().withText("value 3"); // by text
        assertThat(select.getFirstSelectedOption().getText()).isEqualTo("value 3");
    }

    @Test
    public void checkClearAction() {
        goTo(DEFAULT_URL);
        assertThat(findFirst("#name").getValue()).contains("John");
        findFirst("#name").clear();
        assertThat($("#name").first().getValue()).isEqualTo("");
    }

    @Test
    public void checkClickAction() {
        goTo(DEFAULT_URL);
        assertThat(title()).contains("Selenium");
        findFirst("#linkToPage2").click();
        assertThat(title()).isEqualTo("Page 2");
    }
}
