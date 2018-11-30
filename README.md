# ktlint-sparta-rule-set
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

Работа с правилами выполняется посредством плагина `ktlint-maven-plugin`. Проект является его зависимостью.

### Поключение с помощью Maven
```
<plugin>
    <groupId>com.github.z3d1k</groupId>
    <artifactId>ktlint-maven-plugin</artifactId>
    <version>...</version>
    <executions>
        ...
    </executions>
    <dependencies>
        <dependency>
            <groupId>com.github.hellbat-community</groupId>
            <artifactId>ktlint-sparta-rule-set</artifactId>
            <version>...</version>
        </dependency>
    </dependencies>
</plugin>
```

## Конфигурация `ktlint-sparta-rule-set.json`
Конфигурация правил настраивается в корне директории в файле `ktlint-sparta-rule-set.json`.

Пример: 
```
{
    "java-doc" : true,
    "max-depth-nested": 2,
    "cyclomatic-complexity": 2,
    "max-line-length": 120
}
```
### Список правил:

_Примечание: Правило будет включено в проверку, если соблюден формат из описания._ 

**`class-name-contains`** _(Array)_ - проверяет вхождение в название класса строки `name-part` _(String)_ в пакете 
`package-name` _(String)_. 
Работает только для классов.

Работает в двух режимах: 

`end` _(String)_ - проверяет с конца

`start` _(String)_ - проверяет с начала
 
```
{
    ...
    "class-name-contains" :[
            {
                "param-name": "end",
                "package-name": "form",
                "name-part": "Form"
            },
            {
                "param-name": "start",
                "package-name": "ui",
                "name-part": "UI"
            }
    ]
    ...
}
```

**`fun-arguments`**  _(Int)_ - проверяет колличество передаваемых аргументов в функциях.
```
{
    ...
    "max-fun-arguments" :5
    ...
}
```

**`fun-name-pattern`** _(String Regexp)_ - проверяет, что название функции соответсвует шаблону.
```
{
    ...
    "fun-name-pattern" : "[a-z][a-zA-Z]*"
    ...
}
```

**`cyclomatic-complexity`** _(Int)_ - проверяет цикломатическую сложность кода.
```
{
    ...
    "cyclomatic-complexity" : 3
    ...
}
```

**`java-docs`** _(Boolean)_ - проверяет документцию по коду
```
{
    ...
    "java-docs" : true
    ...
}
```

**`ban-layers`** _(Array)_ - проверяет, что в выбраном пакете `goal` _(String)_ на слое `root` _(String)_не 
используются пакеты с этого слоя `unexpected-layers` _(Array < String >)_ 

```
{
    ...
    "ban-layers": [
        {
            "goal": "pl",
            "root": "main.package",
            "unexpected-layers": [
                "pl",
                "bs",
                "da"
            ]
        },
        {
            "goal": "da",
            "root": "main.package",
            "unexpected-layers": [
                "pl",
                "bs"
            ]
        },
        {
            "goal": "bs",
            "root": "main.package",
            "unexpected-layers": [
                "da"
            ]
        }
    ]
    ...
}
```

**`max-line-length`**  _(Int)_  проверяет длину строк по коду.
```
{
    ...
    "max-line-length" : 120
    ...
}
```

**`max-depth-nested`**  _(Int)_  проверяет вложенность блоков по коду.
```
{
    ...
    "max-depth-nested" : 3
    ...
}
```

**`node-body-max-length-rule`**  _(Array)_  проверяет длинну `max-length` _(Int)_ блока кода для выбранного узла 
`type` _(String)_ . Пока `type` принимает значение только `fun` и `class`.
```
{
    ...
    {
        "type": "class",
        "max-length": 7
    },
    {
        "type": "fun",
        "max-length": 20
    }
    ...
}
```

**`node-name-length-rule`**  _(Array)_  проверяет длинну `max-length` _(Int)_ названия для выбранного узла 
`type` _(String )_ . Пока `type` принимает значение только `fun` и `class`.
```
{
    ...
    {
        "type": "class",
        "max-length": 7
    },
    {
        "type": "fun",
        "max-length": 20
    }
    ...
}
```

**`package-use-not-twice`** _(Array < String >)_ проверяет, что выбранный пакет не используется дважды в коде, 
например логер, вызваный два раза. 
```
{
    ...
    "package-not-use-twice": [
        "org.slf4j.Logger"
    ]
    ...
}
```