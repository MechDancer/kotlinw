# kotlinw
[![Build Status](https://www.travis-ci.org/MechDancer/kotlinw.svg?branch=master)](https://www.travis-ci.org/MechDancer/kotlinw)

意为 *kotlin compiler api wrapper*，对 kotlin 编译器进行简单 (~~草率~~) 封装，使其能够快速从 `String` 源代码编译到 class 文件。

## 概述

本项目核心内容包括两部分：

* `Analyzer`
* `Compiler`

在两者的配合下能够对源码进行解析、编译。两者都为工具类，由静态函数组成。

## Analyzer

该工具类下有 `parse` 和 `analyze` 两个函数。

## parse

想要对一个 `String` 源码，首先要将其解析为 `KtFile` 实例。该函数接收字符串源码，并调用 `PsiFileFactory` 完成这项工作，将 `KtFile` 返回。

## analyze

将源码解析到 `KtFile` 后，使用 `AnalyzingUtils` 和 `TopDownAnalyzerFacadeForJVM` 检查代码静态错误，将分析结果返回。

## Compiler

该工具类下有三个函数 `compile`、`writeToFile`、`loadClasses`。

## compile

该函数会将参数传入的 `KtFile` 实例调用 ` Analyzer.analyze`，获得分析结果。在这之后创建 `GenerationState`，并使用 `KotlinCodegenFacade` 进行编译，返回 `GenerationState`，其中包含着编译结果。

### writeToFile

该函数接收 `GenerationState` 编译结果和一个文件夹，将编译完成的类写入该文件夹。

### loadClasses

该函数接收 `GenerationState` 编译结果，将编译完成的类使用内部类加载器加载，返回 `List<Class<*>>`。

## 编译过程

源码 (`String`) -> `KtFile` -> `KtFile` + `AnalysisResult` -> `GenerationState` -> `Class`

>部分函数调用需要 `KotlinCoreEnvironment `作为参数传入。它附带了一些编译信息和编译配置，只需使用 `EnvironmentUtil.createEnvironment()` 创建一个即可。

## 示例

```kotlin
val src = """
			fun a() = run {
				"ok"
			}
""".trimIndent()

val environment = EnvironmentUtil.createEnvironment()
val ktFile = Analyzer.parse(src, environment)
val result = Compiler.compile(ktFile, environment)
val classes = Compiler.loadClasses(result)

classes[0].getMethod("a")(null).let(::println)
```

这样会在屏幕上打印 "ok"。您可以在 [这里](https://github.com/MechDancer/kotlinw/blob/master/src/test/kotlin/org/mechdancer/kotlinw/test/TestCompile.kt) 找到完整代码。