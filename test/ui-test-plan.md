# PotatoBot UI Test Plan

This plan is consumed by `skills/test-ui/scripts/run_ui_tests.py`. Each test case starts with an empty task list. Steps within a test case share state and are checked in order.

The runner requires Java 25, builds before testing, compares each PotatoBot reply exactly after normalizing line endings and surrounding whitespace, prints the console input/output record, and stops at the first failure. A test case can provide its starting save-file content through `initial_save_file`.

```json
{
  "build_command": ["mvn", "test"],
  "run_command": ["java", "-cp", "target/classes", "PotatoBot"],
  "test_cases": [
    {
      "id": "UI-001",
      "aim": "Add each supported typed task, mark one task, and display the stored details.",
      "steps": [
        {
          "input": "todo read book",
          "expected_output": "PotatoBot:\n  Added: read book (Todo)"
        },
        {
          "input": "deadline return book /by Sunday",
          "expected_output": "PotatoBot:\n  Added: return book (Deadline, by: Sunday)"
        },
        {
          "input": "event project meeting /from Mon 2pm /to 4pm",
          "expected_output": "PotatoBot:\n  Added: project meeting (Event, from: Mon 2pm to: 4pm)"
        },
        {
          "input": "mark 1",
          "expected_output": "PotatoBot:\n  Task completed: read book (Todo)\n  Keep going!"
        },
        {
          "input": "list",
          "expected_output": "PotatoBot:\n  Here are the tasks in your list:\n    1.[X] read book (Todo)\n    2.[ ] return book (Deadline, by: Sunday)\n    3.[ ] project meeting (Event, from: Mon 2pm to: 4pm)"
        }
      ]
    },
    {
      "id": "UI-002",
      "aim": "Reject a non-integer task number without terminating PotatoBot.",
      "steps": [
        {
          "input": "add buy potatoes",
          "expected_output": "PotatoBot:\n  Added: buy potatoes"
        },
        {
          "input": "mark 24.2F",
          "expected_output": "PotatoBot:\n  Do you hear yourself?? Task number must be a whole number."
        },
        {
          "input": "list",
          "expected_output": "PotatoBot:\n  Here are the tasks in your list:\n    1.[ ] buy potatoes"
        }
      ]
    },
    {
      "id": "UI-003",
      "aim": "Delete a task by number and remove it from the displayed list.",
      "steps": [
        {
          "input": "add buy potatoes",
          "expected_output": "PotatoBot:\n  Added: buy potatoes"
        },
        {
          "input": "todo read book",
          "expected_output": "PotatoBot:\n  Added: read book (Todo)"
        },
        {
          "input": "delete 1",
          "expected_output": "PotatoBot:\n  Task deleted: buy potatoes\n  Keep it going!"
        },
        {
          "input": "list",
          "expected_output": "PotatoBot:\n  Here are the tasks in your list:\n    1.[ ] read book (Todo)"
        }
      ]
    },
    {
      "id": "UI-004",
      "aim": "Save the current task list before exiting PotatoBot.",
      "steps": [
        {
          "input": "add buy potatoes",
          "expected_output": "PotatoBot:\n  Added: buy potatoes"
        },
        {
          "input": "bye",
          "expected_output": "PotatoBot:\n  Tasks saved to ./data/potatabot.txt\n\n================================================================================\nBye. I'm rolling back to the potato patch. Hope to see you again soon!\n================================================================================"
        }
      ]
    },
    {
      "id": "UI-005",
      "aim": "Load saved tasks and restore their types and completion states on startup.",
      "initial_save_file": "[X] read book (Todo)\n[ ] return book (Deadline, by: Sunday)\n[ ] project meeting (Event, from: Mon 2pm to: 4pm)",
      "steps": [
        {
          "input": "list",
          "expected_output": "PotatoBot:\n  Here are the tasks in your list:\n    1.[X] read book (Todo)\n    2.[ ] return book (Deadline, by: Sunday)\n    3.[ ] project meeting (Event, from: Mon 2pm to: 4pm)"
        }
      ]
    }
  ]
}
```
