$(document).ready(function () {
    // Add smooth scrolling to all links
    $("a").on('click', function (event) {

        // Make sure this.hash has a value before overriding default behavior
        if (this.hash !== "") {
            // Prevent default anchor click behavior
            event.preventDefault();

            // Store hash
            var hash = this.hash;

            // Using jQuery's animate() method to add smooth page scroll
            // The optional number (800) specifies the number of milliseconds it takes to scroll to the specified area
            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 2000, function () {
                // Add hash (#) to URL when done scrolling (default click behavior)
                /* window.location.hash = hash; */
            });
        } // End if
    });


// // define window position and scroll tracking variables
// var windowHeight, windowScrollPosTop, windowScrollPosBottom = 0;
// var objectOffsetTop = $('#line').offset().top;
// var count = 5;

// // calculate window position and scroll tracking variables
// function calcScrollValues() {
//     windowHeight = jQuery(window).height();
//     windowScrollPosTop = jQuery(window).scrollTop();
//     windowScrollPosBottom = windowHeight + windowScrollPosTop;

//     // if the page has been scrolled far enough to reveal the element
//     if (windowScrollPosBottom > objectOffsetTop) {
//         var lastScrollTop = 0;
//         $(window).scroll(function(event){
//            var st = $(this).scrollTop();
//            if (st > lastScrollTop){
//             $('#line').css('height', count++);
//            } else {
//               console.log('Upwards Scroll');
//            }
//            lastScrollTop = st;
//         });
//     }else{
//         console.log('Not Visible');
//     } // end if the page has scrolled enough check

// } // end calcScrollValues


// // run the following on initial page load
// calcScrollValues();

// // run the following on every scroll event
// jQuery(window).scroll(function() {
//     calcScrollValues()
// }); // end on scroll

});