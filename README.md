To run tests in IDE:
1. Open project;
2. Open pom.xml
3. Run maven to install all dependencies
2. Right click on testng.xml;
3. Click Run '...\testng.xml'

Pages contains only fields and methods required for test (can be extended for next test cases).

REPORTING IN ALLURE (after 1st run of tests):
1. Download Allure files: https://github.com/allure-framework/allure2/releases
2. Unzip
3. Add to PATH
4. Start CMD and go to project catalog (eg. cd D:\WORKSPACE\XM\xm_recrut)
5. Run command 'allure serve'
Report should run new browser window.