<?
include ( 'includes/jpgraph/src/jpgraph.php');
include ('includes/jpgraph/src/jpgraph_antispam.php' );

$spam = new  AntiSpam(); 
$chars = $spam-> Rand(5);

if( $spam->Stroke () === false  ) {
    die("Illegal or no data to plot");
}
?>