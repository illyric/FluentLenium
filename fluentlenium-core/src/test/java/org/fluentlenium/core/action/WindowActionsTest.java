package org.fluentlenium.core.action;

import com.google.common.collect.ImmutableSet;
import org.assertj.core.api.Assertions;
import org.fluentlenium.configuration.Configuration;
import org.fluentlenium.core.FluentControl;
import org.fluentlenium.core.FluentDriver;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.core.wait.FluentWait;
import org.fluentlenium.core.wait.FluentWaitWindowConditions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WindowActionsTest {
    @Mock
    private WebDriver driver;
    @Mock
    private WebDriver.Window window;
    @Mock
    private WebDriver.Options options;
    @Mock
    private FluentDriver fluentDriver;
    @Mock
    private FluentControl fluentControl;
    @Mock
    private WebDriver.TargetLocator targetLocator;

    @Before
    public void before() {
        when(fluentDriver.getDriver()).thenReturn(driver);
        when(driver.manage()).thenReturn(options);
        when(driver.manage().window()).thenReturn(window);
        when(driver.switchTo()).thenReturn(targetLocator);
        when(driver.switchTo().window(anyString())).thenReturn(driver);
    }

    @After
    public void after() {
        reset(driver, window, fluentDriver);
    }

    @Test
    public void getWindowTest() {
        final WindowAction windowAction = new WindowAction(fluentDriver, driver);

        windowAction.getWindow();
        verify(driver.manage(), times(1)).window();
    }

    @Test
    public void maximizeWindowTest() {
        final WindowAction windowAction = new WindowAction(fluentDriver, driver);

        windowAction.maximizeWindow();
        verify(driver.manage(), times(1)).window();
        verify(driver.manage().window(), times(1)).maximize();
    }

    @Test
    public void fullScreenWindowTest() {
        final WindowAction windowAction = new WindowAction(fluentDriver, driver);

        windowAction.fullScreen();
        verify(driver.manage(), times(1)).window();
        verify(driver.manage().window(), times(1)).fullscreen();
    }

    @Test
    public void switchToTest() {
        final String windowHandle = "WndH1";

        final WindowAction windowAction = new WindowAction(fluentDriver, driver);

        when(driver.getWindowHandle()).thenReturn(windowHandle);

        windowAction.switchTo(windowHandle);

        verify(driver, times(1)).manage();
        verify(driver, times(2)).switchTo();
    }

    @Test
    public void switchToLast() {
        final String windowHandle = "WndH1";
        final String windowHandle2 = "WndH2";

        final WindowAction windowAction = new WindowAction(fluentDriver, driver);

        when(driver.getWindowHandles()).thenReturn(ImmutableSet.of(windowHandle, windowHandle2));

        windowAction.switchToLast();

        verify(driver, times(1)).manage();
        verify(driver, times(2)).switchTo();
    }

    @Test
    public void openNewAndSwitch() {
        final JavascriptWebDriver jsDriver = mock(JavascriptWebDriver.class);

        when(fluentDriver.getDriver()).thenReturn(jsDriver);
        when(jsDriver.manage()).thenReturn(options);
        when(jsDriver.manage().window()).thenReturn(window);
        when(jsDriver.switchTo()).thenReturn(targetLocator);
        when(jsDriver.switchTo().window(anyString())).thenReturn(driver);

        final String windowHandle = "WndH1";
        final String windowHandle1 = "WndH2";
        final String windowHandle2 = "WndH3";

        final FluentWait fluentWait = mock(FluentWait.class);
        final FluentWaitWindowConditions fluentWaitWindowMatcher = mock(FluentWaitWindowConditions.class);
        final Configuration configuration = mock(Configuration.class);

        final FluentDriver currentFluentDriver = new FluentDriver(driver, configuration, fluentControl);
        final FluentDriver fluentDriverSpied = spy(currentFluentDriver);

        when(jsDriver.getWindowHandles()).thenReturn(ImmutableSet.of(windowHandle, windowHandle1),
                ImmutableSet.of(windowHandle, windowHandle1, windowHandle2));
        when(jsDriver.getWindowHandle()).thenReturn(windowHandle1, windowHandle2);

        when(fluentWait.untilWindow(anyString())).thenReturn(fluentWaitWindowMatcher);
        final WindowAction windowAction = new WindowAction(fluentDriverSpied, jsDriver);
        windowAction.openNewAndSwitch();

        verify(jsDriver, times(1)).manage();
        verify(jsDriver, times(1)).getWindowHandle();
        verify(jsDriver, times(3)).getWindowHandles();
        verify(jsDriver, times(2)).switchTo();

    }

    @Test
    public void switchToParentFrame() {
        final WindowAction windowAction = new WindowAction(fluentDriver, driver);

        windowAction.switchToParentFrame();

        verify(driver, times(1)).manage();
        verify(driver.switchTo(), times(1)).parentFrame();
    }

    @Test
    public void setSizeTest() {
        final WindowAction windowAction = new WindowAction(fluentDriver, driver);
        final Dimension dim = new Dimension(100, 200);
        windowAction.setSize(dim);
        verify(driver.manage(), times(1)).window();
        verify(driver.manage().window(), times(1)).setSize(eq(dim));
    }

    @Test
    public void clickAndCloseCurrentTest() throws InterruptedException {
        final String windowHandle = "WndH1";
        final String windowHandle2 = "WndH2";

        final FluentWebElement fluentWebElement = mock(FluentWebElement.class);
        final FluentWait fluentWait = mock(FluentWait.class);
        final FluentWaitWindowConditions fluentWaitWindowMatcher = mock(FluentWaitWindowConditions.class);

        when(driver.getWindowHandles()).thenReturn(ImmutableSet.of(windowHandle, windowHandle2));
        when(fluentWaitWindowMatcher.notDisplayed()).thenReturn(true);
        when(fluentWebElement.click()).thenReturn(fluentWebElement);
        when(fluentWait.untilWindow(anyString())).thenReturn(fluentWaitWindowMatcher);
        when(fluentDriver.await()).thenReturn(fluentWait);

        final WindowAction windowAction = new WindowAction(fluentDriver, driver);
        windowAction.clickAndCloseCurrent(fluentWebElement);

        verify(driver, times(1)).manage();
        verify(driver, times(1)).getWindowHandle();

    }

    @Test
    public void clickAndOpenNewTest() throws InterruptedException {
        final String windowHandle = "WndH1";
        final String windowHandle1 = "WndH2";
        final String windowHandle2 = "WndH3";

        final FluentWebElement fluentWebElement = mock(FluentWebElement.class);
        final FluentWait fluentWait = mock(FluentWait.class);
        final FluentWaitWindowConditions fluentWaitWindowMatcher = mock(FluentWaitWindowConditions.class);
        final Configuration configuration = mock(Configuration.class);

        final FluentDriver currentFluentDriver = new FluentDriver(driver, configuration, fluentControl);
        final FluentDriver fluentDriverSpied = spy(currentFluentDriver);

        when(driver.getWindowHandles()).thenReturn(ImmutableSet.of(windowHandle, windowHandle1),
                ImmutableSet.of(windowHandle, windowHandle1, windowHandle2));
        when(driver.getWindowHandle()).thenReturn(windowHandle1, windowHandle2);

        when(fluentWebElement.click()).thenReturn(fluentWebElement);

        when(fluentWait.untilWindow(anyString())).thenReturn(fluentWaitWindowMatcher);
        final WindowAction windowAction = new WindowAction(fluentDriverSpied, driver);
        windowAction.clickAndOpenNew(fluentWebElement);

        verify(driver, times(3)).manage();
        verify(driver, times(3)).getWindowHandles();
    }

    @Test
    public void getSizeTest() {
        final WindowAction windowAction = new WindowAction(fluentDriver, driver);
        final Point pos = new Point(101, 201);

        when(driver.manage().window().getPosition()).thenReturn(pos);

        final Point getPos = windowAction.getPosition();

        verify(driver.manage(), times(2)).window();
        verify(driver.manage().window(), times(1)).getPosition();
        Assertions.assertThat(getPos).isEqualTo(pos);
    }

    @Test
    public void getPositionTest() {
        final WindowAction windowAction = new WindowAction(fluentDriver, driver);
        final Dimension dim = new Dimension(101, 201);

        when(driver.manage().window().getSize()).thenReturn(dim);

        final Dimension getSizeDim = windowAction.getSize();

        verify(driver.manage(), times(2)).window();
        verify(driver.manage().window(), times(1)).getSize();
        Assertions.assertThat(getSizeDim).isEqualTo(dim);
    }

    @Test
    public void setPositionTest() {
        final WindowAction windowAction = new WindowAction(fluentDriver, driver);
        final Point pos = new Point(101, 201);

        windowAction.setPosition(pos);
        verify(driver.manage(), times(1)).window();
        verify(driver.manage().window(), times(1)).setPosition(eq(pos));
    }

    @Test
    public void titleTest() {
        final String title = "title";
        final WindowAction windowAction = new WindowAction(fluentDriver, driver);
        when(driver.getTitle()).thenReturn(title);

        Assertions.assertThat(windowAction.title()).isEqualTo(title);

        verify(driver.manage(), times(0)).window();
    }

    @Test
    public void closeTest() {
        final WindowAction windowAction = new WindowAction(fluentDriver, driver);
        windowAction.close();

        verify(driver.manage(), times(0)).window();
        verify(driver, times(1)).close();
    }

    public interface JavascriptWebDriver extends WebDriver, JavascriptExecutor {

    }
}
