# Wytyczne do Projektu

## Biblioteka do testowania

Sposób uruchomienia: java -jar $ścieżkaDoWaszegoJara $ścieżkaDoMojegoProjektu

Wymogi ogólne: Wymogiem jest możliwość stworzenia i uruchomienia jara. mvn package ma nie zwracać ani jednego błędu, i maksymalnie 8 warningów. Nie zaakceptuję prezentacji aplikacji z poziomu IDE.

Pokrycie testami minimum 80%. Oczekuję dowodu, więc albo będziecie testować już istniejącą biblioteką, albo w waszym narzędziu stworzycie moduł do obliczania code coverage

#### Krok 1 -
 uruchomienie testów; @Test oraz assertTrue() // Refleksja, Wyjątki Wyszukuje i uruchamia wszystkie metody z adnotacją @Test Wyszukuje i uruchamia wszystkie publiczne voidowe metody w klasach z adnotacją @Test Po wykonaniu testu w konsoli wyświetla się wynik wykonania.

#### Krok 2 - 
łańcuchy testów; Adnotacja @DependsOn // Refleksja, Strumienie i Lambdy Test może zależeć od 1 lub większej ilości testów. Test zostaje uruchomiony TYLKO jeśli wszystkie testy od których zależy przeszły. Możliwe jest wielopoziomowe łańcuchowanie. C zależy od B; B zależy od A. W tym przypadku jeśli A przejdzie, a B nie, C nie zostanie wywołane. @DependsOn przyjmuje Stringa, lub tablicę Stringów (up to you). Raport z wykonania pojawia się dopiero po zakończeniu pracy z łańcuchem.

#### Krok 3 - 
grupy testów; Adnotacja @Group // Refleksja, Strumienie, Lambdy i Wielowątkowość Testy z tej samej grupy uruchamiane są na 1 wątku, równolegle do pozostałych grup. Innymi słowy - zdefiniowanie 3 grup testów sprawia, że testy wykonują się na 3 wątkach. Łańcuchy testów zaczynają tu być pewnym problemem. Oczekuję, że powiecie mi jakim oraz jakie są możliwe rozwiązania. Raporty z wykonania pojawia się dopiero po zakończeniu pracy z grupą. Raporty różnych grup nie wchodzą sobie w drogę - niemożliwe jest drukowanie z obu wątków jednocześnie.



###### Ekstra funkcjonalności -> kolejność dowolna

EF 1 - @Timeout(int ms) // Wielowątkowość Jeśli czas trwania wykonania testu przekracza limit podany w milisekundach, test nie przechodzi.

EF 2 - @Test(repeat = n) // Algorytmika, clean code Test może zostać wykonany n razy.

EF 3 - @ValueSource, @Test(valueSource = "nazwaMetodyPomocniczej") // Refleksja Test może być parametryzowany. Innymi słowy - metoda z adnotacją @Test przyjmuje parametry (np. Person person, String newSurname). Dane te są zaciągnięte z metody pomocniczej z adnotacją @ValueSource. Podpowiedź -> zerknijcie sobie na @ValueSource z JUnit, lub (nawet lepiej) na @DataProvider z TestNG.

// Refleksja, clean code i algorytmika EF 4 - @BeforeEach @AfterEach Metody uruchamiane przed/po każdym teście w danej klasie

EF 5 - @BeforeAll @AfterAll Metody uruchamiane przed/po wszystkich testach w danej klasie

EF 6 - @BeforeGroup @AfterGroup Metody uruchamiane przed/po danej grupie testów.
