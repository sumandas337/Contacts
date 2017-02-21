package domain.com.contacts;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import domain.com.contacts.utils.StringUtils;

/**
 * Created by sumandas on 10/02/2017.
 */

public class StringUtilsTest extends TestCase {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    @Test
    public void test_is_null_orEmpty() {
        assertFalse(StringUtils.isNotNullOrEmpty(null));
        assertFalse(StringUtils.isNotNullOrEmpty(""));
        assertTrue(StringUtils.isNotNullOrEmpty("hello"));
    }

    @Test
    public void test_Valid_Number(){
        assertTrue(StringUtils.isValidPhoneNumber("+91123456789012"));
        assertTrue(StringUtils.isValidPhoneNumber("123456789012"));
        assertTrue(StringUtils.isValidPhoneNumber("0123456789012"));
        assertFalse(StringUtils.isValidPhoneNumber("+0123456789012"));
        assertFalse(StringUtils.isValidPhoneNumber("+911234567890123"));
        assertFalse(StringUtils.isValidPhoneNumber("91123456789012"));
        assertFalse(StringUtils.isValidPhoneNumber("111"));
    }

    @Test
    public void test_valid_name(){
        assertTrue(StringUtils.isValidName("abc"));
        assertFalse(StringUtils.isValidName(null));
        assertFalse(StringUtils.isValidName(""));
        assertFalse(StringUtils.isValidName("aa"));
    }
}
