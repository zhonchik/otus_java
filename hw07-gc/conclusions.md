# Результаты сравнения GC (PS и G1)

Предположим, что мы разрабатываем программу подготовки отчётов.
Будем оптимизировать скорость подготовки отчёта 

## Результаты выполнения

Для PS в таблице указаны значения Scavenge/MarkSweep
Для G1 в таблице указаны значения Young Generation/Old Generation
Количество обработанных элементов приведено с точностью до 1000

|                                   | PS 4Gb     | G1 4 Gb    | PS 256Mb   | G1 256Mb   |
|:----------------------------------|:----------:|:----------:|:----------:|:----------:|
| Количество запусков GC            | 31/16      | 97/36      | 11/15      | 29/8       |
| Время GC max                      | 210/2117   | 96/3346    | 36/381     | 24/136     |
| Время GC avg                      | 108/1036   | 31/2235    | 22/162     | 8/126      |
| Время GC total                    | 3366/23002 | 3019/80471 | 244/2432   | 259/1011   |
| Количество обработанных элементов | 22000      | 30000      | 1000       | 1000       |

## Выводы

* G1 чаще запускает сборку мусора для молодого поколения по отношению к старому, чем PS
* У G1 сборка мусора для молодого поколения требует меньше времени, чем у PS
* У PS сборка мусора для старого поколения требует меньше времени, чем у G1
* Суммарное время работы GC для G1 больше, чем для PS. Но нужно учесть, что с G1 программа проработала больше времени (
это видно в логах) и обработала больше данных прежде чем случился OOM. Это значит, что G1 быстрее, чем PS освобождает
память, что позволяет хранить больше "полезных" данных
* Для нашей задачи больше подходит G1

## Приложение. Логи выполнения

### PS 4Gb

```
> Task :hw07-gc:GcCheck.main()
New listener for PS MarkSweep
New listener for PS Scavenge
[1797] PS Scavenge: max 22, avg 22, total 22, count 1
[3466] PS Scavenge: max 31, avg 26, total 53, count 2
[6932] PS Scavenge: max 55, avg 36, total 108, count 3
[6987] PS MarkSweep: max 271, avg 271, total 271, count 1
1000 items processed
[11622] PS Scavenge: max 55, avg 38, total 153, count 4
2000 items processed
[17133] PS Scavenge: max 61, avg 42, total 214, count 5
[17194] PS MarkSweep: max 427, avg 349, total 698, count 2
[20877] PS Scavenge: max 61, avg 43, total 261, count 6
3000 items processed
[24199] PS Scavenge: max 71, avg 47, total 332, count 7
[27735] PS Scavenge: max 86, avg 52, total 418, count 8
4000 items processed
[31237] PS Scavenge: max 100, avg 57, total 518, count 9
[34847] PS Scavenge: max 100, avg 60, total 604, count 10
5000 items processed
[38451] PS Scavenge: max 100, avg 63, total 697, count 11
[41818] PS Scavenge: max 106, avg 66, total 803, count 12
[41924] PS MarkSweep: max 612, avg 436, total 1310, count 3
6000 items processed
[45805] PS Scavenge: max 131, avg 71, total 934, count 13
[49749] PS Scavenge: max 131, avg 72, total 1011, count 14
7000 items processed
[53646] PS Scavenge: max 139, avg 76, total 1150, count 15
[57168] PS Scavenge: max 139, avg 80, total 1286, count 16
8000 items processed
[60687] PS Scavenge: max 139, avg 83, total 1421, count 17
[64830] PS Scavenge: max 166, avg 88, total 1587, count 18
9000 items processed
[69101] PS Scavenge: max 166, avg 91, total 1746, count 19
[69260] PS MarkSweep: max 1024, avg 583, total 2334, count 4
10000 items processed
[75112] PS Scavenge: max 210, avg 97, total 1956, count 20
[80165] PS Scavenge: max 210, avg 99, total 2084, count 21
11000 items processed
[86106] PS Scavenge: max 210, avg 100, total 2208, count 22
[86230] PS MarkSweep: max 1100, avg 686, total 3434, count 5
12000 items processed
[93119] PS Scavenge: max 210, avg 105, total 2418, count 23
13000 items processed
[100088] PS Scavenge: max 210, avg 105, total 2526, count 24
14000 items processed
[106691] PS Scavenge: max 210, avg 105, total 2630, count 25
15000 items processed
[114363] PS Scavenge: max 210, avg 105, total 2736, count 26
[114469] PS MarkSweep: max 1726, avg 860, total 5160, count 6
16000 items processed
[124459] PS Scavenge: max 210, avg 102, total 2780, count 27
17000 items processed
[132709] PS Scavenge: max 210, avg 102, total 2878, count 28
18000 items processed
[141289] PS Scavenge: max 210, avg 106, total 3079, count 29
19000 items processed
20000 items processed
[150051] PS Scavenge: max 210, avg 105, total 3168, count 30
21000 items processed
[158977] PS Scavenge: max 210, avg 108, total 3366, count 31
[159175] PS MarkSweep: max 2117, avg 1039, total 7277, count 7
22000 items processed
[169997] PS MarkSweep: max 2117, avg 1125, total 9001, count 8
[173142] PS MarkSweep: max 2117, avg 1183, total 10653, count 9
[175499] PS MarkSweep: max 2117, avg 1247, total 12477, count 10
[177671] PS MarkSweep: max 2117, avg 1293, total 14230, count 11
[179603] PS MarkSweep: max 2117, avg 1317, total 15814, count 12
[181274] PS MarkSweep: max 2117, avg 1326, total 17250, count 13
[182756] PS MarkSweep: max 2117, avg 1378, total 19294, count 14
[184822] PS MarkSweep: max 2117, avg 1402, total 21044, count 15
[186585] PS MarkSweep: max 2117, avg 1437, total 23002, count 16
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at ru.otus.Benchmark.run(Benchmark.java:10)
	at ru.otus.GcCheck.main(GcCheck.java:12)

> Task :hw07-gc:GcCheck.main() FAILED

Execution failed for task ':hw07-gc:GcCheck.main()'.
> Process 'command '/usr/local/Cellar/openjdk/13.0.2+8_2/libexec/openjdk.jdk/Contents/Home/bin/java'' finished with non-zero exit value 1
```

### G1 4Gb

```
> Task :hw07-gc:GcCheck.main()
New listener for G1 Young Generation
New listener for G1 Old Generation
[722] G1 Young Generation: max 7, avg 7, total 7, count 1
[1039] G1 Young Generation: max 10, avg 8, total 17, count 2
[1707] G1 Young Generation: max 15, avg 10, total 32, count 3
[2507] G1 Young Generation: max 15, avg 11, total 45, count 4
[3524] G1 Young Generation: max 18, avg 12, total 63, count 5
[4968] G1 Young Generation: max 40, avg 17, total 103, count 6
[6502] G1 Young Generation: max 40, avg 17, total 125, count 7
1000 items processed
[7909] G1 Young Generation: max 40, avg 18, total 147, count 8
[9142] G1 Young Generation: max 40, avg 18, total 168, count 9
[10824] G1 Young Generation: max 40, avg 18, total 187, count 10
[12303] G1 Young Generation: max 40, avg 18, total 203, count 11
[13565] G1 Young Generation: max 40, avg 18, total 218, count 12
[14583] G1 Young Generation: max 40, avg 17, total 228, count 13
2000 items processed
[15304] G1 Young Generation: max 40, avg 17, total 239, count 14
[16444] G1 Young Generation: max 40, avg 16, total 251, count 15
[17471] G1 Young Generation: max 40, avg 16, total 267, count 16
[18749] G1 Young Generation: max 40, avg 16, total 281, count 17
[19995] G1 Young Generation: max 40, avg 16, total 298, count 18
[21492] G1 Young Generation: max 40, avg 16, total 311, count 19
3000 items processed
[22823] G1 Young Generation: max 40, avg 16, total 329, count 20
[24518] G1 Young Generation: max 40, avg 16, total 347, count 21
[26049] G1 Young Generation: max 40, avg 16, total 367, count 22
[27768] G1 Young Generation: max 40, avg 16, total 387, count 23
[29334] G1 Young Generation: max 40, avg 16, total 405, count 24
4000 items processed
[31453] G1 Young Generation: max 40, avg 17, total 427, count 25
[33408] G1 Young Generation: max 40, avg 17, total 450, count 26
[35896] G1 Young Generation: max 40, avg 17, total 474, count 27
5000 items processed
[38129] G1 Young Generation: max 40, avg 17, total 500, count 28
[40698] G1 Young Generation: max 40, avg 18, total 525, count 29
[43274] G1 Young Generation: max 40, avg 18, total 556, count 30
6000 items processed
[46511] G1 Young Generation: max 40, avg 18, total 585, count 31
[49442] G1 Young Generation: max 40, avg 19, total 615, count 32
7000 items processed
[52844] G1 Young Generation: max 47, avg 20, total 662, count 33
[56227] G1 Young Generation: max 47, avg 20, total 701, count 34
8000 items processed
[60405] G1 Young Generation: max 47, avg 21, total 746, count 35
[63904] G1 Young Generation: max 47, avg 21, total 791, count 36
9000 items processed
[68539] G1 Young Generation: max 57, avg 22, total 848, count 37
[72880] G1 Young Generation: max 57, avg 23, total 895, count 38
10000 items processed
[78010] G1 Young Generation: max 63, avg 24, total 958, count 39
11000 items processed
[82996] G1 Young Generation: max 71, avg 25, total 1029, count 40
12000 items processed
[88739] G1 Young Generation: max 71, avg 26, total 1091, count 41
[94373] G1 Young Generation: max 83, avg 27, total 1174, count 42
13000 items processed
[101047] G1 Young Generation: max 83, avg 28, total 1243, count 43
14000 items processed
[107296] G1 Young Generation: max 95, avg 30, total 1338, count 44
15000 items processed
[114774] G1 Young Generation: max 95, avg 31, total 1433, count 45
16000 items processed
[121476] G1 Young Generation: max 95, avg 32, total 1508, count 46
17000 items processed
[130371] G1 Young Generation: max 96, avg 34, total 1604, count 47
18000 items processed
19000 items processed
[138860] G1 Young Generation: max 96, avg 35, total 1683, count 48
20000 items processed
[147986] G1 Young Generation: max 96, avg 36, total 1773, count 49
21000 items processed
[156150] G1 Young Generation: max 96, avg 36, total 1844, count 50
22000 items processed
[163103] G1 Young Generation: max 96, avg 37, total 1907, count 51
23000 items processed
[168793] G1 Young Generation: max 96, avg 37, total 1966, count 52
[173432] G1 Young Generation: max 96, avg 37, total 2011, count 53
24000 items processed
[177167] G1 Young Generation: max 96, avg 37, total 2050, count 54
[180276] G1 Young Generation: max 96, avg 37, total 2086, count 55
25000 items processed
[183276] G1 Young Generation: max 96, avg 37, total 2115, count 56
[186337] G1 Young Generation: max 96, avg 37, total 2144, count 57
[189359] G1 Young Generation: max 96, avg 37, total 2170, count 58
26000 items processed
[192446] G1 Young Generation: max 96, avg 37, total 2199, count 59
[195542] G1 Young Generation: max 96, avg 37, total 2231, count 60
27000 items processed
[198527] G1 Young Generation: max 96, avg 36, total 2255, count 61
[201623] G1 Young Generation: max 96, avg 36, total 2288, count 62
28000 items processed
[204720] G1 Young Generation: max 96, avg 37, total 2346, count 63
[207123] G1 Young Generation: max 96, avg 38, total 2442, count 64
[207219] G1 Old Generation: max 2127, avg 2127, total 2127, count 1
[212810] G1 Young Generation: max 96, avg 37, total 2468, count 65
29000 items processed
[215874] G1 Young Generation: max 96, avg 38, total 2548, count 66
[217420] G1 Young Generation: max 96, avg 39, total 2618, count 67
[217490] G1 Old Generation: max 2127, avg 2036, total 4072, count 2
[221806] G1 Young Generation: max 96, avg 39, total 2693, count 68
[221881] G1 Old Generation: max 2127, avg 1982, total 5948, count 3
30000 items processed
[225368] G1 Young Generation: max 96, avg 39, total 2754, count 69
[225429] G1 Old Generation: max 2127, avg 1981, total 7926, count 4
[228483] G1 Young Generation: max 96, avg 39, total 2794, count 70
[228523] G1 Old Generation: max 2127, avg 2004, total 10020, count 5
[231281] G1 Young Generation: max 96, avg 39, total 2824, count 71
[231311] G1 Old Generation: max 2341, avg 2060, total 12361, count 6
[234136] G1 Young Generation: max 96, avg 39, total 2847, count 72
[234159] G1 Old Generation: max 2341, avg 2091, total 14638, count 7
[236829] G1 Young Generation: max 96, avg 39, total 2865, count 73
[236847] G1 Old Generation: max 2341, avg 2102, total 16821, count 8
[239266] G1 Young Generation: max 96, avg 38, total 2879, count 74
[239280] G1 Old Generation: max 2341, avg 2105, total 18948, count 9
[241573] G1 Young Generation: max 96, avg 38, total 2891, count 75
[241585] G1 Old Generation: max 2341, avg 2102, total 21022, count 10
[243795] G1 Young Generation: max 96, avg 38, total 2899, count 76
[243803] G1 Old Generation: max 2341, avg 2105, total 23164, count 11
[246023] G1 Young Generation: max 96, avg 37, total 2908, count 77
[246032] G1 Old Generation: max 2341, avg 2113, total 25367, count 12
[248335] G1 Young Generation: max 96, avg 37, total 2917, count 78
[248344] G1 Old Generation: max 2341, avg 2114, total 27482, count 13
[250509] G1 Young Generation: max 96, avg 37, total 2925, count 79
[250517] G1 Old Generation: max 2344, avg 2130, total 29826, count 14
[252907] G1 Young Generation: max 96, avg 36, total 2931, count 80
[252913] G1 Old Generation: max 2609, avg 2162, total 32435, count 15
[255559] G1 Young Generation: max 96, avg 36, total 2938, count 81
[255566] G1 Old Generation: max 2609, avg 2165, total 34648, count 16
[257842] G1 Young Generation: max 96, avg 35, total 2945, count 82
[257849] G1 Old Generation: max 2609, avg 2184, total 37140, count 17
[260361] G1 Young Generation: max 96, avg 35, total 2949, count 83
[260365] G1 Old Generation: max 2609, avg 2181, total 39258, count 18
[262509] G1 Young Generation: max 96, avg 35, total 2953, count 84
[262513] G1 Old Generation: max 2609, avg 2187, total 41558, count 19
[264860] G1 Young Generation: max 96, avg 34, total 2958, count 85
[264865] G1 Old Generation: max 2609, avg 2190, total 43811, count 20
[267131] G1 Young Generation: max 96, avg 34, total 2963, count 86
[267136] G1 Old Generation: max 2609, avg 2189, total 45989, count 21
[269327] G1 Young Generation: max 96, avg 34, total 2970, count 87
[269334] G1 Old Generation: max 2609, avg 2196, total 48321, count 22
[271687] G1 Young Generation: max 96, avg 33, total 2976, count 88
[271693] G1 Old Generation: max 2609, avg 2191, total 50413, count 23
[273814] G1 Young Generation: max 96, avg 33, total 2982, count 89
[273820] G1 Old Generation: max 2609, avg 2188, total 52531, count 24
[275953] G1 Young Generation: max 96, avg 33, total 2988, count 90
[275959] G1 Old Generation: max 2609, avg 2183, total 54591, count 25
[278033] G1 Young Generation: max 96, avg 32, total 2996, count 91
[278041] G1 Old Generation: max 2609, avg 2184, total 56794, count 26
[280265] G1 Young Generation: max 96, avg 32, total 3003, count 92
[280272] G1 Old Generation: max 2609, avg 2187, total 59073, count 27
[282564] G1 Young Generation: max 96, avg 32, total 3010, count 93
[282571] G1 Old Generation: max 2663, avg 2204, total 61736, count 28
[285234] G1 Old Generation: max 2663, avg 2206, total 63992, count 29
[287490] G1 Young Generation: max 96, avg 32, total 3011, count 94
[287491] G1 Old Generation: max 3346, avg 2244, total 67338, count 30
[290837] G1 Old Generation: max 3346, avg 2250, total 69777, count 31
[293289] G1 Young Generation: max 96, avg 31, total 3017, count 95
[293295] G1 Old Generation: max 3346, avg 2249, total 71999, count 32
[295538] G1 Old Generation: max 3346, avg 2246, total 74138, count 33
[297677] G1 Old Generation: max 3346, avg 2245, total 76355, count 34
[299894] G1 Young Generation: max 96, avg 31, total 3018, count 96
[299895] G1 Old Generation: max 3346, avg 2241, total 78454, count 35
[301994] G1 Old Generation: max 3346, avg 2235, total 80471, count 36
[304011] G1 Young Generation: max 96, avg 31, total 3019, count 97
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at ru.otus.Benchmark.run(Benchmark.java:10)
	at ru.otus.GcCheck.main(GcCheck.java:12)

> Task :hw07-gc:GcCheck.main() FAILED

Execution failed for task ':hw07-gc:GcCheck.main()'.
> Process 'command '/usr/local/Cellar/openjdk/13.0.2+8_2/libexec/openjdk.jdk/Contents/Home/bin/java'' finished with non-zero exit value 1
```

### PS 256Mb

```
> Task :hw07-gc:GcCheck.main()
New listener for PS MarkSweep
New listener for PS Scavenge
[1115] PS Scavenge: max 21, avg 21, total 21, count 1
[2093] PS Scavenge: max 22, avg 21, total 43, count 2
[3091] PS Scavenge: max 22, avg 20, total 62, count 3
[4150] PS Scavenge: max 24, avg 21, total 86, count 4
[5242] PS Scavenge: max 36, avg 24, total 122, count 5
[6309] PS Scavenge: max 36, avg 26, total 156, count 6
[6802] PS Scavenge: max 36, avg 23, total 167, count 7
1000 items processed
[7294] PS Scavenge: max 36, avg 22, total 181, count 8
[7760] PS Scavenge: max 36, avg 22, total 198, count 9
[8290] PS Scavenge: max 36, avg 22, total 222, count 10
[8825] PS Scavenge: max 36, avg 22, total 244, count 11
[8847] PS MarkSweep: max 381, avg 381, total 381, count 1
[9722] PS MarkSweep: max 381, avg 260, total 521, count 2
[10266] PS MarkSweep: max 381, avg 219, total 659, count 3
[10667] PS MarkSweep: max 381, avg 197, total 790, count 4
[10959] PS MarkSweep: max 381, avg 187, total 937, count 5
[11219] PS MarkSweep: max 381, avg 179, total 1074, count 6
[11435] PS MarkSweep: max 381, avg 175, total 1226, count 7
[11653] PS MarkSweep: max 381, avg 171, total 1375, count 8
[11836] PS MarkSweep: max 381, avg 168, total 1512, count 9
[11999] PS MarkSweep: max 381, avg 167, total 1674, count 10
[12180] PS MarkSweep: max 381, avg 166, total 1834, count 11
[12354] PS MarkSweep: max 381, avg 165, total 1990, count 12
[12518] PS MarkSweep: max 381, avg 164, total 2136, count 13
[12670] PS MarkSweep: max 381, avg 163, total 2285, count 14
Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
[12819] PS MarkSweep: max 381, avg 162, total 2432, count 15
	at ru.otus.Benchmark.run(Benchmark.java:10)
	at ru.otus.GcCheck.main(GcCheck.java:12)

> Task :hw07-gc:GcCheck.main() FAILED

Execution failed for task ':hw07-gc:GcCheck.main()'.
> Process 'command '/usr/local/Cellar/openjdk/13.0.2+8_2/libexec/openjdk.jdk/Contents/Home/bin/java'' finished with non-zero exit value 1
```

### G1 256Mb

```
> Task :hw07-gc:GcCheck.main()
New listener for G1 Young Generation
New listener for G1 Old Generation
[830] G1 Young Generation: max 11, avg 11, total 11, count 1
[1207] G1 Young Generation: max 11, avg 8, total 16, count 2
[1991] G1 Young Generation: max 12, avg 9, total 28, count 3
[3048] G1 Young Generation: max 15, avg 10, total 43, count 4
[4448] G1 Young Generation: max 22, avg 13, total 65, count 5
[5951] G1 Young Generation: max 22, avg 14, total 86, count 6
[7240] G1 Young Generation: max 24, avg 15, total 110, count 7
1000 items processed
[8339] G1 Young Generation: max 24, avg 15, total 126, count 8
[9218] G1 Young Generation: max 24, avg 15, total 139, count 9
[9916] G1 Young Generation: max 24, avg 15, total 151, count 10
[10398] G1 Young Generation: max 24, avg 14, total 158, count 11
[10751] G1 Young Generation: max 24, avg 13, total 164, count 12
[11014] G1 Young Generation: max 24, avg 13, total 169, count 13
[11291] G1 Young Generation: max 24, avg 12, total 173, count 14
[11572] G1 Young Generation: max 24, avg 11, total 177, count 15
[11846] G1 Young Generation: max 24, avg 11, total 188, count 16
[12125] G1 Young Generation: max 24, avg 11, total 194, count 17
[12396] G1 Young Generation: max 24, avg 10, total 197, count 18
[12665] G1 Young Generation: max 24, avg 10, total 201, count 19
[12936] G1 Young Generation: max 24, avg 10, total 204, count 20
[13207] G1 Young Generation: max 24, avg 10, total 211, count 21
[13432] G1 Young Generation: max 24, avg 10, total 221, count 22
[13442] G1 Old Generation: max 129, avg 129, total 129, count 1
[13843] G1 Young Generation: max 24, avg 10, total 232, count 23
[13854] G1 Old Generation: max 129, avg 129, total 258, count 2
[14138] G1 Young Generation: max 24, avg 9, total 237, count 24
[14143] G1 Old Generation: max 136, avg 131, total 394, count 3
[14386] G1 Young Generation: max 24, avg 9, total 240, count 25
[14389] G1 Old Generation: max 136, avg 132, total 530, count 4
[14576] G1 Young Generation: max 24, avg 9, total 244, count 26
[14580] G1 Old Generation: max 136, avg 128, total 640, count 5
[14743] G1 Young Generation: max 24, avg 9, total 251, count 27
[14750] G1 Old Generation: max 136, avg 129, total 776, count 6
[14909] G1 Young Generation: max 24, avg 9, total 254, count 28
[14912] G1 Old Generation: max 136, avg 128, total 896, count 7
[15060] G1 Young Generation: max 24, avg 8, total 259, count 29
[15065] G1 Old Generation: max 136, avg 126, total 1011, count 8
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at ru.otus.Benchmark.run(Benchmark.java:10)
	at ru.otus.GcCheck.main(GcCheck.java:12)

> Task :hw07-gc:GcCheck.main() FAILED

Execution failed for task ':hw07-gc:GcCheck.main()'.
> Process 'command '/usr/local/Cellar/openjdk/13.0.2+8_2/libexec/openjdk.jdk/Contents/Home/bin/java'' finished with non-zero exit value 1
```
