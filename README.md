# Amazon keyword score

### Framework / libraries

* JDK 1.11
* Spring boot 2.1.8
* Maven
* Spring Cloud 2020.0.3

Run Application
* git clone https://github.com/shadabgada/amazon-keyword-score.git
* cd amazon-keyword-score
> mvn spring-boot:run

### Endpoint:

````
GET: localhost:8080/estimate?keyword=amazon alexa echo
````


### 1. Assumptions

* It looks from the amazon website, below endpoint is used for getting the responses.
  * https://completion.amazon.com/api/2017/suggestions.
* If a keyword comes up in the result **EXACTLY** , it is generally a hot keyword and I have assumed to give it significantly higher points.
* The tricky part is when result don't match exactly. In that case I have used *Levenshtein distance* algorithm as described below

### 2. Algorithm

* If a keyword comes up in the result **EXACTLY** 3 times, it is one of the hottest keyword. In this case, score would be 100.
* **Levenshtein distance Algorithm**:
  * The Levenshtein distance is a measure of dissimilarity between two Strings. Mathematically, given two Strings x and y, the distance measures the minimum number of character edits required to transform x into y.

  * Typically, three type of edits are allowed:
    * Insertion of a character c
    * Deletion of a character c
    * Substitution of a character c with c‘


### 3. Accuracy

* It's not accurate. I believe this implementation would definitely give rough idea of trending keywords based on their scores returned
* some keywords and their scores for reference.
  * amazon alexa echo: 89
  * amazon alexa echo dot: 92
  * micromax canvas silver: 9
  * iphone 11: 75
  * iphone 11 pro: 80
