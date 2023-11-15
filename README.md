# INSTRUCTIONS FOR THIS TEST

This test aims to test knowledge of java, spring and sql 

Domain model is created - marketing campaign  consists of several questions.
Each question consist of:
 - **type** : can be RATING (number of stars for example 1 to 5), or CHOICE (user can select one to many options).
 - **text**: question text
 - **options**: possible options when type CHOICE

Users can than submit Feedback to the campaign:

Feedback entity:
 - belongs to campaign
 - contains list of answers to each question from campaign

Answer:
 - either contains ratingValue (int) or list of selected options for question

# GOAL

Implement REST API to return summary of campaign, I have defined return object of the API. Please use Spring Data 

It should return:
- number of total feedbacks in the campaign
- list of questions summary
  - for each summary it should return its name and type
  - if type == RATING, it should calculate average of rating values
  - if type == CHOICE, it should return number of occurrences of each option


There is import.sql with some dummy data as an example. For this data, it should return:

- total feedbacks = 3
- question 1, rating average = 4
- question 2
  - option 1 = 1 occurrence 
  - option 2 = 3 occurrences
  - option 3 = 0 occurrences
  - option 4 = 2 occurrences


## TRY TO MAKE CODE AS CLEAN AND PERFORMANT AS POSSIBLE!

# Solution
Steps that led to desired output:
1. Repositories extending JpaRepository and custom queries

Calculating avarage rating
```sql
@Query("SELECT AVG(a.ratingValue) FROM Answer a WHERE a.question.id = :questionId AND a.ratingValue > 0")
    BigDecimal calculateAverageRating(@Param("questionId") UUID questionId);
```

Calculating occurences of the options
```sql
@Query(value = "SELECT \"o\".\"id\", \"o\".\"text\", COUNT(\"aso\".\"option_id\") " +
            "FROM \"option\" \"o\" " +
            "LEFT JOIN \"answer_selected_option\" \"aso\" ON \"o\".\"id\" = \"aso\".\"option_id\" " +
            "WHERE \"o\".\"question_id\" = :questionId " +
            "GROUP BY \"o\".\"id\", \"o\".\"text\"", nativeQuery = true)
    List<Object[]> countOptionOccurrences(@Param("questionId") UUID questionId);
```

2. CampaignService class where we are setting feedbacks and question summaries

```java
public CampaignSummary getCampaignSummary(UUID campaignId) {
    CampaignSummary summary = new CampaignSummary();
    summary.setTotalFeedbacks(feedbackService.countFeedbackByCampaignId(campaignId));
    summary.setQuestionSummaries(questionService.getQuestionSummaries(campaignId));
    return summary;
}
```

3. FeedbackService class where we are counting feedbacks using JPA

```java
    public long countFeedbackByCampaignId(UUID campaignId) {return feedbackRepository.countByCampaignId(campaignId);}
}
```

4. QuestionService differentiates between question types (RATING or CHOICE) and compiles their summaries.
```java
public List<QuestionSummary> getQuestionSummaries(UUID campaignId) {
List<Question> questions = questionRepository.findByCampaignId(campaignId);
List<QuestionSummary> questionSummaries = new ArrayList<>();

        for (Question question : questions) {
            QuestionSummary questionSummary = new QuestionSummary();
            questionSummary.setName(question.getText());
            questionSummary.setType(question.getType());

            if (question.getType() == QuestionType.RATING) {
                questionSummary.setRatingAverage(answerService.calculateAverageRating(question.getId()));
                questionSummary.setOptionSummaries(Collections.emptyList());
            } else {
                questionSummary.setOptionSummaries(answerService.countOptionOccurrences(question.getId()));
                questionSummary.setRatingAverage(BigDecimal.ZERO);
            }
            questionSummaries.add(questionSummary);
        }

        return questionSummaries;
    }
}
```

5. Answerservice class provides the logic for calculating the average rating for RATING type questions and for counting the occurrences of each option in CHOICE type questions.
```java
public BigDecimal calculateAverageRating(UUID questionId) {
        return answerRepository.calculateAverageRating(questionId);
        }

public List<OptionSummary> countOptionOccurrences(UUID questionId) {
        List<Object[]> queryResults = optionRepository.countOptionOccurrences(questionId);
        List<OptionSummary> optionSummaries = new ArrayList<>();

        for (Object[] result : queryResults) {
        String optionText = (String) result[1];
        int occurrences = ((Number) result[2]).intValue();

        OptionSummary optionSummary = new OptionSummary();
        optionSummary.setText(optionText);
        optionSummary.setOccurrences(occurrences);
        optionSummaries.add(optionSummary);
        }

        return optionSummaries;
}
```

6. Controller that calls campaignService.

```java
   @GetMapping("/summary/{uuid}")
   public ResponseEntity<CampaignSummary> getSummary(@PathVariable UUID uuid) {
   CampaignSummary summary = campaignService.getCampaignSummary(uuid);
   return ResponseEntity.ok(summary);
   }
```

7. Manually overriding toString method so the infinite loop is removed. 

## Output

```json
{
  "totalFeedbacks":3,
  "questionSummaries":[
    {
      "name":"Rating question",
      "type":"RATING",
      "ratingAverage":4.0,
      "optionSummaries":[
      ]
    },
    {
      "name":"Choice question",
      "type":"CHOICE",
      "ratingAverage":0,
      "optionSummaries":[
        {
          "text":"Option 1",
          "occurrences":1
        },
        {
          "text":"Option 2",
          "occurrences":3
        },
        {
          "text":"Option 3",
          "occurrences":0
        },
        {
          "text":"Option 4",
          "occurrences":2
        }
      ]
    }
  ]
}
```
