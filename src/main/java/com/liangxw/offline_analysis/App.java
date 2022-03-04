package com.liangxw.offline_analysis;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;

import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;

public class App {
        public static void main(String[] args) {
                System.setProperty("webdriver.chrome.driver", "C:\\maven\\apache-maven-3.5.4\\bin\\chromedriver.exe");
                System.setProperty("webdriver.chrome.bin",
                                "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
                // 创建无Chrome无头参数
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                ChromeDriver driver = new ChromeDriver(options);
                try {
                        Operation.doSomeop(driver);
                } finally {
                        driver.quit();
                }

        }
}

class Operation {
        public Operation() {
        }

        public static Map<String, ArrayList<String>> dynamicValue = new HashMap<String, ArrayList<String>>();
        static {
                dynamicValue.put("domain", new ArrayList<String>() {
                        {
                                add("https://author1-qa65.aia.adobecqms.net");
                        }
                });// qa -> stage -->prod
                dynamicValue.put("active_tree_path",
                                new ArrayList<String>() {
                                        {
                                                add("/content/dam/id-vitality/fragment/en/rewards_v2");
                                                add("/content/dam/id-vitality/fragment/en/rewards_v2/partner-details");
                                        }
                                });
                dynamicValue.put("editor_page", new ArrayList<String>() {
                        {
                                add("/editor.html/content/id-vitality/en/api/whats-on.html");
                        }
                });
        }

        public static Map<String, String> map_main = new HashMap<String, String>();
        static {

                map_main.put("login", "/libs/granite/core/content/login.html");
                map_main.put("activeTree", "/libs/replication/treeactivation.html");
                map_main.put("dispatch_env", "stage");// id/ph/th
                map_main.put("dispatch_env_pro", "id");// id/ph/th
        }
        public static Map<String, String> uat_map_dispatch = new HashMap<String, String>();
        static {
                uat_map_dispatch.put("stage_id",
                                "/etc/acs-commons/dispatcher-flush/id-vitality-flush.html");
                uat_map_dispatch.put("stage_ph",
                                "/etc/acs-commons/dispatcher-flush/ph-vitality-flush.html");
                uat_map_dispatch.put("stage_th",
                                "/etc/acs-commons/dispatcher-flush/th-vitality.html");
                uat_map_dispatch.put("qa_id",
                                "/etc/acs-commons/dispatcher-flush/id-vitality-flush.html");
                uat_map_dispatch.put("qa_ph",
                                "/etc/acs-commons/dispatcher-flush/ph-vitality-flush.html");
                uat_map_dispatch.put("qa_th",
                                "/etc/acs-commons/dispatcher-flush/th-vitality.html");

                uat_map_dispatch.put("prod_id",
                                "/etc/acs-commons/dispatcher-flush/id-vitality.html");
                uat_map_dispatch.put("prod_ph",
                                "/etc/acs-commons/dispatcher-flush/ph-vitality-flush.html");
                uat_map_dispatch.put("prod_th",
                                "/etc/acs-commons/dispatcher-flush/th-vitality.html");
        }

        public static void doSomeop(ChromeDriver driver) {
                Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                                .withTimeout(Duration.ofSeconds(10))
                                .pollingEvery(Duration.ofSeconds(2))
                                .ignoring(NoSuchElementException.class);

                driver.get(dynamicValue.get("domain").get(0) + map_main.get("login"));
                // 登陆
                WebElement inputName = driver.findElement(By.cssSelector("#username"));
                inputName.sendKeys("");
                WebElement password = driver.findElement(By.cssSelector("#password"));
                password.sendKeys("");
                WebElement button = driver.findElement(By.cssSelector("#submit-button"));
                Actions actionProvider2 = new Actions(driver);
                actionProvider2.click(button).build().perform();
                new WebDriverWait(driver, Duration.ofSeconds(1))
                                .until(ExpectedConditions.urlContains("aem/start.html"));
                System.out.println("1.login in 当前：" + driver.getTitle());

                // active tree 打开 -- 输入并刷新
                driver.switchTo().newWindow(WindowType.WINDOW);
                driver.get(dynamicValue.get("domain").get(0) + map_main.get("activeTree"));
                WebElement activeTree_input = new WebDriverWait(driver,
                                Duration.ofSeconds(2))
                                                .until(driver3 -> driver3.findElement(By.cssSelector(
                                                                "#ext-comp-1007")));
                System.out.println("2.active tree 当前：" + driver.getTitle());

                dynamicValue.get("active_tree_path").forEach(link -> {
                        activeTree_input.clear();

                        driver.executeScript("document.querySelector('#path').value='" + link
                                        + "';document.querySelector('#ext-comp-1007').value='"
                                        + link + "'");
                        Actions actionProvider3 = new Actions(driver);
                        // active tree 激活按钮
                        WebElement activeBtn = driver.findElement(By.cssSelector(
                                        "#treeProgress_form > table > tbody > tr:nth-child(5) > td:nth-child(2) > input[type=button]:nth-child(3)"));
                        actionProvider3.click(activeBtn).build().perform();
                        new WebDriverWait(driver, Duration.ofSeconds(3));

                });

                // 打开对应的编辑页面刷新对应的标签功能并发布
                driver.switchTo().newWindow(WindowType.WINDOW);
                driver.get((String) dynamicValue.get("domain").get(0)
                                + (String) dynamicValue.get("editor_page").get(0));
                WebElement tiptop = new WebDriverWait(driver, Duration.ofSeconds(1))
                                .until(ExpectedConditions.elementToBeClickable(By.cssSelector(
                                                "#pageinfo-trigger")));
                Actions actionProvider4 = new Actions(driver);
                actionProvider4.click(tiptop).build().perform();

                System.out.println("3. editor.html 当前:" + driver.getTitle());
                WebElement opprator_action = new WebDriverWait(driver, Duration.ofSeconds(6))
                                .until(ExpectedConditions
                                                .elementToBeClickable(By.xpath("//*[@id='pageinfo-data']/button[1]")));
                actionProvider4.click(opprator_action).build().perform();

                WebElement save_close_btn = new WebDriverWait(driver, Duration.ofSeconds(6))
                                .until(ExpectedConditions
                                                .elementToBeClickable(
                                                                By.cssSelector("#shell-propertiespage-doneactivator")));
                actionProvider4.click(save_close_btn).build().perform();

                tiptop = new WebDriverWait(driver, Duration.ofSeconds(6))
                                .until(ExpectedConditions.elementToBeClickable(By.cssSelector(
                                                "#pageinfo-trigger")));
                actionProvider4.click(tiptop).build().perform();

                WebElement opprator_action2 = new WebDriverWait(driver,
                                Duration.ofSeconds(6))
                                                .until(ExpectedConditions
                                                                .elementToBeClickable(By.xpath(
                                                                                "//*[@id='pageinfo-data']/button[4]")));
                actionProvider4.click(opprator_action2).build().perform();

                // 打开dispatch
                driver.switchTo().newWindow(WindowType.WINDOW);
                driver.get(dynamicValue.get("domain").get(0) +
                                uat_map_dispatch.get(
                                                map_main.get("dispatch_env") + "_" + map_main.get("dispatch_env_pro")));
                WebElement flushBtn = new WebDriverWait(driver, Duration.ofSeconds(2))
                                .until(ExpectedConditions.elementToBeClickable((By.cssSelector(
                                                "#cq-gen19 > form > input"))));

                System.out.println("4.dispatch 当前：" + driver.getTitle());
                Actions actionProvider5 = new Actions(driver);

                actionProvider5.click(flushBtn).build().perform();
                System.out.println("执行完成");

        }

}