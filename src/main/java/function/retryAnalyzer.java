package function;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class retryAnalyzer extends functional implements IRetryAnalyzer {
    /**
     * @param COUNTER: total time retried, set default = 0 and will increase after retry
     * @param RETRY_LIMIT: total time retry, if set = n it'll retry n times, set = 0 won't retry
     * Note: Don't forget to set listener to main XML
     */
    int COUNTER = 0;
    int RETRY_LIMIT = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (COUNTER < RETRY_LIMIT) {
            COUNTER++;
            return true;
        }
        // auto create issue on Jira if all retries were failure
        return false;
    }
}
