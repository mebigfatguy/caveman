A library containing standard collections using primitive types, including

List
Set
Map
Bag
Deque

Each collection type can be used with any primitive type, 

boolean
byte
char
short
int
long
float
double

There are interfaces defined for each primitive type, in the package

com.mebigfatguy.caveman

such as

IntList
CharSet
ByteBag

For Maps there are actually three kinds:

primitive->primitive
primitive->Object
Object->primitive

In the case where an Object is used, 
typical generics notation is expected, so, interfaces like

IntLongMap
CharKeyMap<V>          // a map with a key type of char
ShortValueMap<K>    // a map with a value type of short

For each combination of primitive there are class implementations in the package

com.mebigfatguy.caveman.impl

These classes following the naming of the interface but are prefixed by CaveMan, so

IntList l = new CaveManIntList();
ByteSet s = new CaveManByteSet();
FloatBag b = new CaveManFloatBag();

and for maps,

IntDoubleMap m = new CaveManIntDoubleMap();
CharKeyMap<Integer> m = new CaveManCharKeyMap<Integer>();
DoubleValueMap<String> m = new CaveManDoubleValueMap<String>();

On top of the available interfaces, classes implement a 'collection' interface with a primitive name, such as

FloatCollection

Caveman Collections is available on [maven.org](http://search.maven.org/#search%7Cga%7C1%7Ccaveman)

       GroupId: com.mebigfatguy.caveman
    ArtifactId: caveman
       Version: 0.2.0
