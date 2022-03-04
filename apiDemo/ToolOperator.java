package com.liangxw.offline_analysis;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.locators.RelativeLocator;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class ToolOperator {
    public static void SetProxy_method() {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy("<HOST:PORT>");
        ChromeOptions options = new ChromeOptions();
        options.setCapability("proxy", proxy);
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.google.com/");
        driver.manage().window().maximize();
        driver.quit();
    }

    public static void SetGetCookie(WebDriver driver) {

        driver.manage().addCookie(new Cookie("key", "value"));

        Cookie cookie1 = driver.manage().getCookieNamed("key");
        System.out.println(cookie1);

        Set<Cookie> cookies = driver.manage().getCookies();
        System.out.println(cookies);

        // delete a cookie with name 'test1'
        driver.manage().deleteCookieNamed("test1");
        // by passing cookie object of current browsing context
        driver.manage().deleteCookie(cookie1);
        driver.manage().deleteAllCookies();

        // 此属性允许用户引导浏览器控制cookie, 是否与第三方站点发起的请求一起发送. 引入其是为了防止CSRF（跨站请求伪造）攻击.
        Cookie cookie = new Cookie.Builder("key", "value").sameSite("Strict /  Lax").build();
        cookie.getSameSite();

    }

    public static void withIframe(WebDriver driver) {
        // 存储网页元素
        WebElement iframe = driver.findElement(By.cssSelector("#modal>iframe"));
        // 切换到 frame
        driver.switchTo().frame(iframe);
        // 现在可以点击按钮
        driver.findElement(By.tagName("button")).click();
        // 使用 ID
        driver.switchTo().frame("buttonframe");
        // 或者使用 name 代替
        driver.switchTo().frame("myframe");
        // 现在可以点击按钮
        driver.findElement(By.tagName("button")).click();
        // 切换到第 2 个框架
        driver.switchTo().frame(1);
        // 回到顶层
        driver.switchTo().defaultContent();

    }

    public static void withWindows(WebDriver driver) {
        // 存储原始窗口的 ID
        String originalWindow = driver.getWindowHandle();

        // 检查一下，我们还没有打开其他的窗口
        assert driver.getWindowHandles().size() == 1;

        // 点击在新窗口中打开的链接
        driver.findElement(By.linkText("new window")).click();

        // 等待新窗口或标签页
        // Wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // 循环执行，直到找到一个新的窗口句柄
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        // 等待新标签完成加载内容
        // wait.until(titleIs("Selenium documentation"));

        // 打开新标签页并切换到新标签页
        driver.switchTo().newWindow(WindowType.TAB);
        // 打开一个新窗口并切换到新窗口
        driver.switchTo().newWindow(WindowType.WINDOW);

        // 关闭标签页或窗口
        driver.close();
        // 切回到之前的标签页或窗口
        driver.switchTo().window(originalWindow);

        // 分别获取每个尺寸
        int width = driver.manage().window().getSize().getWidth();
        int height = driver.manage().window().getSize().getHeight();
        // 或者存储尺寸并在以后查询它们
        Dimension size = driver.manage().window().getSize();
        int width1 = size.getWidth();
        int height1 = size.getHeight();
        // 恢复窗口并设置窗口大小。
        driver.manage().window().setSize(new Dimension(1024, 768));

        // 分别获取每个尺寸
        int x = driver.manage().window().getPosition().getX();
        int y = driver.manage().window().getPosition().getY();
        // 或者存储尺寸并在以后查询它们
        Point position = driver.manage().window().getPosition();
        int x1 = position.getX();
        int y1 = position.getY();

        // 将窗口移动到主显示器的左上角
        driver.manage().window().setPosition(new Point(0, 0));

        // 窗口最大最小全屏
        driver.manage().window().maximize();
        driver.manage().window().minimize();
        driver.manage().window().fullscreen();

        // 全截屏
        Files scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("./image.png"));
        // 元素截屏
        WebElement element = driver.findElement(By.cssSelector("h1"));
        File scrFile2 = element.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile2, new File("./image.png"));

        // Creating the JavascriptExecutor interface object by Type casting
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Button Element
        WebElement button = driver.findElement(By.name("btnLogin"));
        // Executing JavaScript to click on element
        js.executeScript("arguments[0].click();", button);
        // Get return value from script
        String text = (String) js.executeScript("return arguments[0].innerText", button);
        // Executing JavaScript directly
        js.executeScript("console.log('hello world')");

        // 打印
        PrintsPage printer = (PrintsPage) driver;
        PrintOptions printOptions = new PrintOptions();
        printOptions.setPageRanges("1-2");
        Pdf pdf = printer.print(printOptions);
        String content = pdf.getContent();

    }

    public static void finnd_element(WebDriver driver) {
        // 查找元素并输入内容
        WebElement searchInput = driver.findElement(By.name("q"));
        searchInput.sendKeys("selenium");
        // 清除输入的内容
        searchInput.clear();

        // 获取批量元素并遍历它
        List<WebElement> elements_l = driver.findElements(By.tagName("li"));
        for (WebElement element_2 : elements_l) {
            System.out.println("Paragraph text:" + element_2.getText());
        }

        // 定位到激活的元素
        String attr = driver.switchTo().activeElement().getAttribute("title");
        System.out.println(attr);

        // 选择元素的方法 By.tagName("li") By.cssSelector("h1") By.name("q") By.linkText("More
        // information...")
        String value = driver.findElement(By.cssSelector("h1")).getTagName();

        // 坐标及大小 Rectangle getX,getY, getWidth, getHeight methods
        Rectangle res = driver.findElement(By.cssSelector("h1")).getRect();
        // css 样式
        String cssValue = driver.findElement(By.linkText("More information...")).getCssValue("color");

        // 相对定位
        By emailLocator = RelativeLocator.with(By.tagName("input")).above(By.id("password"));
    }

    public static void wait_use(WebDriver driver) {
        // 显示等待
        WebElement firstResult = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//a/h3")));
        // Print the first result
        System.out.println(firstResult.getText());

        WebElement foo = new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(driver -> driver.findElement(By.name("q")));

        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//a/h3")));

        // 隐式等待
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("http://somedomain/url_that_delays_loading");
        WebElement myDynamicElement = driver.findElement(By.id("myDynamicElement"));

        // Waiting 30 seconds for an element to be present on the page, checking
        // for its presence once every 5 seconds.
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);

        WebElement foo3 = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.id("foo"));
            }
        });

    }

    public static void waction(WebDriver driver) {
        // 键盘

        // Enter "webdriver" text and perform "ENTER" keyboard action
        driver.findElement(By.name("q")).sendKeys("webdriver" + Keys.ENTER);

        Actions actionProvider = new Actions(driver);
        Action keydown = actionProvider.keyDown(Keys.CONTROL).sendKeys("a").build();
        keydown.perform();

        WebElement search = driver.findElement(By.name("q"));
        actionProvider.keyDown(Keys.SHIFT).sendKeys(search, "qwerty").keyUp(Keys.SHIFT).sendKeys("qwerty").perform();

        // 鼠标
        WebElement searchBtn2 = driver.findElement(By.linkText("Sign in"));
        Actions actionProvider2 = new Actions(driver);
        // Perform click-and-hold action on the element
        actionProvider2.clickAndHold(searchBtn2).build().perform();

        actionProvider.doubleClick(searchBtn2).build().perform();
        actionProvider.moveToElement(searchBtn2).build().perform();
        // 偏移位置
        actionProvider.moveByOffset(123, 4456).build().perform();
        // 拖拽 参数一 到 参数二
        actionProvider.dragAndDrop(search, searchBtn2).build().perform();
        // 拖拽 到偏移位置释放
        actionProvider.dragAndDropBy(search, 123, 345).build().perform();

        actionProvider.clickAndHold(search).moveToElement(searchBtn2).build().perform();
        // Performs release event
        actionProvider.release().build().perform();

    }

}