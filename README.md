# 🍽️ DobrePAPu - Menadżer Przepisów Kulinarnych

## 📖 O projekcie
**DobrePAPu** to projekt desktopowej aplikacji ułatwiającej gromadzenie, organizację oraz analizę ulubionych przepisów kulinarnych. Naszym celem jest stworzenie intuicyjnego narzędzia, które nie tylko przechowa receptury, ale również dostarczy szczegółowych informacji o wartościach odżywczych każdego posiłku.

## ✨ Główne funkcjonalności

### 🥗 Zarządzanie przepisami
* **Baza przepisów:** Dodawanie, edytowanie i usuwanie własnych przepisów.
* **Segregacja i tagi:** Łatwe wyszukiwanie po tagach (np. *zupa*, *deser*, *wegańskie*).
* **Szczegóły receptury:** Każdy przepis zawiera:
  * Liczbę porcji,
  * Czas przygotowania,
  * Listę kroków / instrukcję przygotowania (DIY),
  * System oceniania w skali 1-5 gwiazdek (⭐),
  * Miejsce na zdjęcie potrawy,
  * Listę składników.

### 🥑 Baza składników
* Rozbudowana baza składników z przypisanymi wartościami odżywczymi.
* Automatyczne zliczanie makroskładników dla każdego posiłku, w tym:
  * Białka,
  * Węglowodanów (w tym cukrów),
  * Tłuszczów,
  * Kalorii.

### 🖥️ Interfejs Użytkownika (GUI)
Aplikacja opiera się na przejrzystym, trójkolumnowym układzie, zapewniającym wygodę użytkowania (wygląd bazujący na interfejsach Outlook i Ania Gotuje). Ekran dzielimy na panele:
1. **Panel nawigacyjny (lewy):** Szybki dostęp do głównych widoków ("Moje przepisy", "Składniki"), profilu użytkownika oraz narzędzi do sortowania.
2. **Panel listy (środkowy):** Pasek wyszukiwania, dynamiczna lista przepisów lub składników oraz przyciski szybkiego dodawania/usuwania (+ / -).
3. **Panel szczegółów (prawy):** Pełny podgląd wybranego elementu – lista składników, kroki przygotowania, ocena, przycisk udostępniania oraz podsumowanie czasu i makroskładników.
![alt text](szkic.png)
## 🛠️ Technologie
* **Język główny / Backend:** Java (obsługa logiki biznesowej, kalkulacje makroskładników).
* **Frontend (GUI):** JavaFX projektowane przy pomocy narzędzia **Scene Builder** (generowanie struktury okien w plikach FXML) oraz kaskadowe arkusze stylów (CSS) do nowoczesnej stylizacji interfejsu.
* **Baza danych:** Wbudowana relacyjna baza **SQLite** (nie wymaga zewnętrznego serwera) obsługiwana przez wbudowany mechanizm **JDBC** (bezpośrednia, przewidywalna komunikacja z bazą za pomocą czystych zapytań SQL).
* **Architektura:** Wzorzec MVC (Model-View-Controller) – wyraźne oddzielenie warstwy danych (bazy), logiki (Javy) i widoku (FXML).

## ⚙️ Inżynieria Oprogramowania i Organizacja Pracy
* **System kontroli wersji:** Git oraz repozytorium na platformie **GitLab** do koordynacji pracy zespołowej.
* **Ciągła integracja (CI):** Wykorzystanie **GitLab CI/CD** do automatycznego budowania aplikacji i uruchamiania testów przy każdym nowym kodzie (push).
* **Testy automatyczne:** Pokrycie kluczowej logiki biznesowej (np. kalkulatora makroskładników i kalorii) testami jednostkowymi z wykorzystaniem biblioteki **JUnit 5**.

## 🚀 Potencjał rozwoju na przyszłość
Projekt został zaprojektowany z myślą o łatwym skalowaniu i rozbudowie w przyszłości. Planowane funkcje to:
* **Rozwój webowy:** Rozbudowa o centralną bazę danych użytkowników i przejście na architekturę klient-serwer.
* **Autentykacja:** Rejestracja, logowanie i bezpieczne przechowywanie haseł użytkowników.
* **Udostępnianie przepisów:** Możliwość eksportu receptur do formatu JSON i dzielenia się nimi z innymi.
* **Zaawansowany kreator:** Dedykowane GUI ułatwiające graficzne dodawanie nowych przepisów (możliwość modyfikacji ręcznej lub importu prosto z pliku JSON).