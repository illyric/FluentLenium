package org.fluentlenium.assertj.custom;

import org.assertj.core.api.AbstractAssert;
import org.fluentlenium.core.alert.Alert;
import org.openqa.selenium.NoAlertPresentException;

/**
 * Alert assertions.
 */
public class AlertAssert extends AbstractAssert<AlertAssert, Alert> {

    /**
     * Creates a new assertion object for alert.
     *
     * @param actual actual alert
     */
    public AlertAssert(final Alert actual) {
        super(actual, AlertAssert.class);
    }

    /**
     * Check that the alert box contains the given text
     *
     * @param text text to search for
     * @return self
     */
    public AlertAssert hasText(final String text) {
        try {
            if (!actual.getText().contains(text)) {
                super.failWithMessage(
                        "The alert box does not contain the text: " + text + " . Actual text found : " + actual.getText());
            }
        } catch (final NoAlertPresentException e) {
            super.failWithMessage("There is no alert box");
        }

        return this;
    }

    /**
     * Check that an alert box is present
     *
     * @return self
     */
    public AlertAssert isPresent() {
        try {
            actual.switchTo();
        } catch (final NoAlertPresentException e) {
            super.failWithMessage("There is no alert box");
        }

        return this;
    }

}
