use m101
db.zips.aggregate([
    
    { $project:
    	{
    		"first_char": {$substr: ["$city", 0, 1]},
    		"pop" : 1
   		}
   	},

	{ $match:
		{
			//"first_char": {$regex: "[0-9]"}
			"first_char": /[0-9]/

		}
	},

	{ $group:
		{
			_id: null,
			popTotal : {$sum: "$pop"}
		}
	}
	,
	{ $project:
    	{
    		"_id": 0,
    		popTotal : 1
   		}
   	}

])
		