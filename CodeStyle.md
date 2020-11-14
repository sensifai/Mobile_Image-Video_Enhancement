# Coding Style

***This Section Describes The Coding Styles and Standards This Library Follows***

**this document focuses primarily on the hard-and-fast rules that we follow universally, and avoids giving advice that isn't clearly enforceable (whether by human or tool)**<br/>
<br/>
## Terminology<br/>
In this document, unless otherwise clarified:<br/>
<br/>
    1. The term class is used inclusively to mean an "ordinary" class, enum class, interface or annotation type (@interface).<br/>
    2. The term member (of a class) is used inclusively to mean a nested class, field, method, or constructor; that is, all top-level contents of a class except initializers and comments.<br/>
    3. The term comment always refers to implementation comments. We do not use the phrase "documentation comments", instead using the common term "Javadoc."<br/>
<br/>
## Source File<br/>

### File Name<br/>
- The source file name consists of the case-sensitive name of the top-level class it contains (of which there is exactly one), plus the .java extension.<br/>
- In the case of resource files the name is a camel-case word plus the .xml extension.<br/>

### File Encoding<br/>
all text files including source files and resource files are encoded in **UTF-8**.<br/>

## Java File Structure<br/>

A source file consists of, in order:<br/>
<br/>
    1. License or copyright information, if present<br/>
    2. Package statement<br/>
    3. Import statements<br/>
    4. Exactly one top-level class<br/>

### License or copyright information, if present<br/>
If license or copyright information belongs in a file, it belongs here.<br/>

**In Case Of Missing License Text The LICENSE File's Text Must Be Considered As The License**

### Package Name<br/>

The package name of the class consists of the directory hierarchy of the file containing the class.<br/>
<br/>
**All Package Names Must be in Lower Case To Avoid Compatibility Issues**<br/>
<br/>
### Import statements<br/>
<br/>
It is highly recommended that all imports be order in a cascading way based on their package names to better organize the imports.<br/>

#### No Wildcard Imports<br/>
Any type of Wildcard imports (static or otherwise) must be avoided.<br/>

### Class Declaration<br/>

#### Exactly one top-level class declaration<br/>
Each top-level class resides in a source file of its own.<br/>

#### Ordering of class contents<br/>
<br/>
The order you choose for the members and initializers of your class can have a great effect on learnability. However, there's no single correct recipe for how to do it; different classes may order their contents in different ways.<br/>
<br/>
It is recommended that the order of members and methods conform the following rules (the higher rule has higher priority):<br/>
<br/>
1. inner classes at the end of the file<br/>
2. members before methods<br/>
3. static before non-static<br/>
4. constant before variable<br/>
5. public before package-private before private<br/>

##### Overloads: never split<br/>
When a class has multiple constructors, or multiple methods with the same name, these appear sequentially, with no other code in between (not even private members).<br/>

## Formatting<br/>

### Braces<br/>

#### Braces are used whenever optional<br/>
Braces are used with `if`, `else`, `for`, `do` and `while` statements, even when the body is empty or contains only a single statement.<br/>

#### Nonempty blocks: K & R style<br/>
<br/>
Braces follow the Kernighan and Ritchie style ("Egyptian brackets") for nonempty blocks and block-like constructs:<br/>
<br/>
    - No line break before the opening brace.<br/>
    - Line break after the opening brace.<br/>
    - Line break before the closing brace.<br/>
    - Line break after the closing brace, only if that brace terminates a statement or terminates the body of a method, constructor, or named class. For example, there is no line break after the brace if it is followed by else or a comma.<br/>
<br/>
Examples:<br/>

```java
return () -> {
  while (condition()) {
    method();
  }
};

return new MyClass() {
  @Override public void method() {
    if (condition()) {
      try {
        something();
      } catch (ProblemException e) {
        recover();
      }
    } else if (otherCondition()) {
      somethingElse();
    } else {
      lastThing();
    }
  }
};
```

#### Empty blocks: may be concise<br/>

An empty block or block-like construct may be in K & R style (as described in Section 4.1.2). Alternatively, it may be closed immediately after it is opened, with no characters or line break in between ({}), unless it is part of a multi-block statement (one that directly contains multiple blocks: `if/else` or `try/catch/finally`).

#### One statement per line<br/>
<br/>
Each statement is followed by a line break.<br/>

## Naming<br/>

### Rules by identifier type<br/>

#### Package names<br/>
<br/>
Package names are all lowercase, with consecutive words simply concatenated together (no underscores).<br/>

#### Class names<br/>

Class names are written in UpperCamelCase.

Class names are typically nouns or noun phrases. For example, `Character` or `ImmutableList`. Interface names may also be nouns or noun phrases (for example, `List`), but may sometimes be adjectives or adjective phrases instead (for example, `Readable`).<br/>
<br/>
There are no specific rules or even well-established conventions for naming annotation types.<br/>
<br/>
Test classes are named starting with the name of the class they are testing, and ending with `Test`. For example, `HashTest` or `HashIntegrationTest`.<br/>

#### Method names<br/>

Method names are written in lowerCamelCase.<br/>

Method names are typically verbs or verb phrases. For example, `sendMessage` or `stop`.<br/>

Underscores may appear in JUnit test method names to separate logical components of the name, with each component written in lowerCamelCase. One typical pattern is `<methodUnderTest>_<state>`, for example `pop_emptyStack`. There is no One Correct Way to name test methods.<br/>

#### Constant names<br/>
<br/>
Constant names use CONSTANT_CASE: all uppercase letters, with each word separated from the next by a single underscore. But what is a constant, exactly?<br/>
<br/>
Constants are static final fields whose contents are deeply immutable and whose methods have no detectable side effects. This includes primitives, Strings, immutable types, and immutable collections of immutable types. If any of the instance's observable state can change, it is not a constant. Merely intending to never mutate the object is not enough. Examples:<br/>

```java
// Constants
static final int NUMBER = 5;
static final ImmutableList<String> NAMES = ImmutableList.of("Ed", "Ann");
static final ImmutableMap<String, Integer> AGES = ImmutableMap.of("Ed", 35, "Ann", 32);
static final Joiner COMMA_JOINER = Joiner.on(','); // because Joiner is immutable
static final SomeMutableType[] EMPTY_ARRAY = {};
enum SomeEnum { ENUM_CONSTANT }

// Not constants
static String nonFinal = "non-final";
final String nonStatic = "non-static";
static final Set<String> mutableCollection = new HashSet<String>();
static final ImmutableSet<SomeMutableType> mutableElements = ImmutableSet.of(mutable);
static final ImmutableMap<String, SomeMutableType> mutableValues =
    ImmutableMap.of("Ed", mutableInstance, "Ann", mutableInstance2);
static final Logger logger = Logger.getLogger(MyClass.getName());
static final String[] nonEmptyArray = {"these", "can", "change"};
```

#### Non-Constant Field names<br/>

Non-constant field names (static or otherwise) are written in lowerCamelCase.<br/>

These names are typically nouns or noun phrases. For example, `computedValues` or `index`.<br/>

#### Parameter names<br/>
<br/>
Parameter names are written in lowerCamelCase.<br/>
<br/>
One-character parameter names in public methods should be avoided.<br/>

#### Local variable names<br/>

Local variable names are written in lowerCamelCase.<br/>
<br/>
Even when final and immutable, local variables are not considered to be constants, and should not be styled as constants.<br/>

## Programming Practices<br/>

### `@Override` : always used<br/>

A method is marked with the `@Override` annotation whenever it is legal. This includes a class method overriding a superclass method, a class method implementing an interface method, and an interface method respecifying a superinterface method.<br/>

Exception: `@Override` may be omitted when the parent method is `@Deprecated`.<br/>

### Caught exceptions: not ignored<br/>

Except as noted below, it is very rarely correct to do nothing in response to a caught exception. (Typical responses are to log it, or if it is considered "impossible", rethrow it as an `AssertionError`.)<br/>
<br/>
When it truly is appropriate to take no action whatsoever in a catch block, the reason this is justified is explained in a comment.<br/>

```java
try {
  int i = Integer.parseInt(response);
  return handleNumericResponse(i);
} catch (NumberFormatException ok) {
  // it's not numeric; that's fine, just continue
}
return handleTextResponse(response);
```

**Exception:** In tests, a caught exception may be ignored without comment if its name is or begins with expected. The following is a very common idiom for ensuring that the code under test does throw an exception of the expected type, so a comment is unnecessary here.<br/>

```java
try {
  emptyStack.pop();
  fail();
} catch (NoSuchElementException expected) {
}
```

### Static members: qualified using class<br/>
<br/>
When a reference to a static class member must be qualified, it is qualified with that class's name, not with a reference or expression of that class's type.<br/>

```java
Foo aFoo = ...;
Foo.aStaticMethod(); // good
aFoo.aStaticMethod(); // bad
somethingThatYieldsAFoo().aStaticMethod(); // very bad
```