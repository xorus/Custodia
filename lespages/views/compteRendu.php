<div class="jumbotron my-jumbotron rounded-0">
	<h1>Compte-rendu</h1>
</div>

<div id='wrapper'>
	<?php 
	    $pdfPath = "documents/compteRendu.pdf";
	    if(file_exists($pdfPath)){ 
	?>

	<div class="container">
		<!-- <object class="fulldoc" data=<?php echo $pdfPath?> type="application/pdf" width="100%" height="100%">
		<embed class="fulldoc" src=<?php echo $pdfPath ?> type="application/pdf">
		    <p>This browser does not support PDFs. Please download the PDF to view it: <a href="Metamorphose.pdf">Download PDF</a>.</p>
		</embed>
		</object> -->
		<iframe src="documents/pdfjs/web/viewer.html?file=../../compteRendu.pdf" class="fulldoc" width="100%" height="100%"></iframe>

		<?php
		    }
		    else{
		        echo "PDF not found, contact site admin.";
		    }
		?>
</script>
	</div>
</div>