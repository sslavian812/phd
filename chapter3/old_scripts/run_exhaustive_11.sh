#!/bin/bash

source ~/WORK/environment/pony-ea-smac/bin/activate

#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py car.csv        11 1>~/logs/exh/car.txt 2>~/logs/exh/errors-car.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py glass.csv      11 1>~/logs/exh/glass.txt 2>~/logs/exh/errors-glass.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py haberman.csv   11 1>~/logs/exh/haberman.txt 2>~/logs/exh/errors-haberman.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py ionosphere.csv 11 1>~/logs/exh/ionosphere.txt 2>~/logs/exh/errors-ionosphere.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py iris.csv       11 1>~/logs/exh/iris.txt 2>~/logs/exh/errors-iris.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py krvskp.csv     11 1>~/logs/exh/krvskp.txt 2>~/logs/exh/errors-krvskp.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py parcinson.csv  11 1>~/logs/exh/parcinson.txt 2>~/logs/exh/errors-parcinson.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py seeds.csv      11 1>~/logs/exh/seeds.txt 2>~/logs/exh/errors-seeds.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py semeion.csv    11 1>~/logs/exh/semeion.txt 2>~/logs/exh/errors-semeion.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py shuttle.csv    11 1>~/logs/exh/shuttle.txt 2>~/logs/exh/errors-shuttle.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py spect.csv      11 1>~/logs/exh/spect.txt 2>~/logs/exh/errors-spect.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py verebral.csv   11 1>~/logs/exh/verebral.txt 2>~/logs/exh/errors-verebral.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py waveform.csv   11 1>~/logs/exh/waveform.txt 2>~/logs/exh/errors-waveform.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py wholesale.csv  11 1>~/logs/exh/wholesale.txt 2>~/logs/exh/errors-wholesale.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py wine.csv       11 1>~/logs/exh/wine.txt 2>~/logs/exh/errors-wine.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py yeast.csv      11 1>~/logs/exh/yeast.txt 2>~/logs/exh/errors-yeast.txt &
#
#wait %1 %2 %3 %4 %5 %6 %7 %8 %9 %10 %11 %12 %13 %14 %15 %16
#
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py backup.csv         11 1>~/logs/exh/backup.txt         2>~/logs/exh/errors-backup.txt         &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py balance.csv        11 1>~/logs/exh/balance.txt        2>~/logs/exh/errors-balance.txt        &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py banknote.csv       11 1>~/logs/exh/banknote.txt       2>~/logs/exh/errors-banknote.txt       &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py indiandiabests.csv 11 1>~/logs/exh/indiandiabests.txt 2>~/logs/exh/errors-indiandiabests.txt &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py tae.csv            11 1>~/logs/exh/tae.txt            2>~/logs/exh/errors-tae.txt            &
#python3 ~/WORK/MultiClustering/ExhaustiveSearch.py websites.csv       11 1>~/logs/exh/websites.txt       2>~/logs/exh/errors-websites.txt       &
#
#wait %1 %2 %3 %4 %5 %6


python3 ~/WORK/MultiClustering/ExhaustiveSearch.py ecoli.csv         11 1>~/logs/exh/ecoli.txt       2>~/logs/exh/errors-ecoli.txt       &
python3 ~/WORK/MultiClustering/ExhaustiveSearch.py flags.csv         11 1>~/logs/exh/flags.txt       2>~/logs/exh/errors-flags.txt       &
python3 ~/WORK/MultiClustering/ExhaustiveSearch.py forestfires.csv   11 1>~/logs/exh/forestfires.txt 2>~/logs/exh/errors-forestfires.txt &
python3 ~/WORK/MultiClustering/ExhaustiveSearch.py leaf.csv          11 1>~/logs/exh/leaf.txt        2>~/logs/exh/errors-leaf.txt        &
wait %1 %2 %3 %4