# datafaker-cli

Provide a commandline interface for datafaker.

# Key features

* show locales defined in the JVM
* show datafaker's provider, and its methods
* evaluate some datafaker's expression
* format evaluation using txt, csv, tsv, json, sql, xml, or yaml
* define format parameters for csv, tsv, etc
* write evaluated data to an output file
* define which locale to use
* define count of sample evaluations

# Examples

## Example 1
Print full-name, and full-address sample.

```
java -jar datafaker-cli.jar
...
locale: en_Us
...
result
"name","address"
"Tobie Goldner","Suite 889 74374 Dean Trail, Lake Wilfredo, LA 43007"
"Mrs. Lou Kohler","Apt. 059 0867 Cora Lock, North Jacqualinetown, NC 30080"
"Beaulah Reichert","449 Becker Plains, North Harrisonside, NY 95819"
```

## Example 2
Print all available locales known to the JVM.

```
java -jar datafaker-cli.jar --available=locales
...
result
Hello org.huberb.datafaker.cli.DatafakerCli
default locale: en-US
locale : af
locale : af-Latn-ZA
locale : af-NA
locale : af-ZA
locale : agq
...
```

## Example 3
Print *all* available datafaker's provider.

```
java -jar datafaker-cli.jar --available=providers
Hello org.huberb.datafaker.cli.DatafakerCli
net.datafaker.providers.base.Address : Address
net.datafaker.providers.base.Ancient : Ancient
net.datafaker.providers.base.Animal : Animal
net.datafaker.providers.base.App : App
...
```
## Example 4
Print all datafaker's providers, and its no-arg methods returning a string.

```
java -jar datafaker-cli.jar --available=providerMethods1
Hello org.huberb.datafaker.cli.DatafakerCli
...
public java.lang.String net.datafaker.providers.base.Address.state()
public java.lang.String net.datafaker.providers.base.Address.country()
public java.lang.String net.datafaker.providers.base.Address.streetAddressNumber()
...
public java.lang.String net.datafaker.providers.base.Address.city()
...
public java.lang.String net.datafaker.providers.base.Ancient.hero()
...
public java.lang.String net.datafaker.providers.base.Animal.scientificName()
public java.lang.String net.datafaker.providers.base.App.name()
...
```

## Example 5
Print evaluate one or more datafaker's expression.

```
java -jar datafaker-cli.jar --expression=expression "#{Name.firstName}" "#{Address.country}"
...
result
"firstName","country"
"Marylyn","Colombia"
"Inge","Uruguay"
"Stephaine","American Samoa"
```


