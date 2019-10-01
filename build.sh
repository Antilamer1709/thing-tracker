cd front
echo "Front install begin"
npm install
npm run build:prod
echo "Front install end"

cd ../
echo "Spring install begin"
mvn clean install -P prod
echo "Spring install end"

echo "Done!"