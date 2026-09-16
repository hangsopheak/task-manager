# Task Manager

The MAD I demo app. Every session is a branch, and every commit is one teaching
step, so the git history is the material: read it top down in class.

```
git log --oneline s03-prep..s03-final   # any session, same shape
```

## Branch index

| Branch | Session | What it teaches |
|---|---|---|
| `s03-prep` | 3 | an untouched Empty Activity project, the one exception to the `-prep` rule of thumb |
| `s03-final` | 3 | first Compose UI: text, layout, the task list screen |
| `s04-final` | 4 | theming and resources: the rebrand, colors and text from resources |
| `s05-final` | 5 | state and recomposition: remember, state hoisting, event lambdas |
| `s06-final` | 6 | navigation between screens and the bottom tab shell |
| `s07-final` | 7 | forms and user input: validation, dialog, the login screen |
| `s09-final` | 9 | Retrofit on the course API, then Firebase Auth |
| `s10-final` | 10 | Room cache and DataStore settings: offline and remembered |
| `s11-final` | 11 | the notification permission, a channel, task reminders |
| `s12-final` | 12 | sensors and location: a task carries a place, the reminder fires on arrival |
| `main` | the finished app | this branch, plus this README |

Session 8 added no app code: it runs the course API and drives it in Postman.
See the `task_management_db` repository.

The plan behind every branch, and the lectures that walk it, live in
Coursesmith: `courses/mad-1/demo/`.
