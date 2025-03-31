```regexp
(var \w+) = 0
$1: Int = 0

 \= false
: Bool = false

(var [\w: =?"]+[^,])\n
$1,\n

(var \w+) \= \"\"
$1: String = ""
```