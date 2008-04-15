@echo off
cp ServiceInterface-aidl.txt IFCPService.aidl
cp -pr ../Common/src/org/freeclinic/common src/org/freeclinic/
move IFCPService.aidl src\org\freeclinic\android\service\
c:\android-sdk_m5-rc14_windows\tools\aidl.exe -Isrc src\org\freeclinic\android\service\IFCPService.aidl
rm src\org\freeclinic\android\service\IFCPService.aidl 
rm -rf src/org/freeclinic/common
echo "Service created!"
