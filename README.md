Get-ChildItem -Path . -Recurse -Directory -Filter ".git" -Force | Where-Object { $_.FullName -ne (Get-Item .).FullName + "\.git" } | Remove-Item -Recurse -Force


git rm -r --cached .
