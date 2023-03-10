package org.hamcrest;

import junit.framework.TestCase;
import org.junit.Assert;

public abstract class AbstractMatcherTest extends TestCase {

  /**
   * Create an instance of the Matcher so some generic safety-net tests can be run on it.
   */
  protected abstract Matcher<?> createMatcher();

  public static <T> void assertMatches(Matcher<T> matcher, T arg) {
      assertMatches("Expected match, but mismatched", matcher, arg);
  }

  public static <T> void assertMatches(String message, Matcher<T> matcher, Object arg) {
    if (!matcher.matches(arg)) {
      Assert.fail(message + " because: '" + mismatchDescription(matcher, arg) + "'");
    }
  }

  public static <T> void assertDoesNotMatch(Matcher<? super T> c, T arg) {
      assertDoesNotMatch("Unexpected match", c, arg);
  }

  public static <T> void assertDoesNotMatch(String message, Matcher<? super T> c, T arg) {
    Assert.assertFalse(message, c.matches(arg));
  }

  public static void assertDescription(String expected, Matcher<?> matcher) {
    Description description = new StringDescription();
    description.appendDescriptionOf(matcher);
    Assert.assertEquals("Expected description", expected, description.toString().trim());
  }

  public static <T> void assertMismatchDescription(String expected, Matcher<? super T> matcher, Object arg) {
    Assert.assertFalse("Precondition: Matcher should not match item.", matcher.matches(arg));
    Assert.assertEquals("Expected mismatch description", expected, mismatchDescription(matcher, arg));
  }

  public static void assertNullSafe(Matcher<?> matcher) {
      try {
          matcher.matches(null);
      }
      catch (Exception e) {
          Assert.fail("Matcher was not null safe");
      }
  }

  public static void assertUnknownTypeSafe(Matcher<?> matcher) {
      try {
          matcher.matches(new UnknownType());
      } catch (Exception e) {
          Assert.fail("Matcher was not unknown type safe, because: " + e);
      }
  }

  public void testIsNullSafe() {
    assertNullSafe(createMatcher());
  }

  public void testCopesWithUnknownTypes() {
    createMatcher().matches(new UnknownType());
  }

    private static <T> String mismatchDescription(Matcher<? super T> matcher, Object arg) {
      Description description = new StringDescription();
      matcher.describeMismatch(arg, description);
      return description.toString().trim();
    }

  @SuppressWarnings("WeakerAccess")
  public static class UnknownType {
  }

}
