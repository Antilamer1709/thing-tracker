cd front
echo "Front install begin"
cmd /C npm install
cmd /C npm run build:prod
echo "Front install end"

cd ../
echo "Spring install begin"
cmd /C mvn clean install
echo "Spring install end"

echo "Done!"