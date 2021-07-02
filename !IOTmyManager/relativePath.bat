
for /R . %%f in (*) do (
  set B=%%f
  echo Relative !B:%CD%\=!
)

Pause